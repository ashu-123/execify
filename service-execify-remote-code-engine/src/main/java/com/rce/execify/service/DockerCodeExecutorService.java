package com.rce.execify.service;

import com.rce.execify.model.dto.CodeRequestDto;
import com.rce.execify.model.dto.CodeResponseDto;
import com.rce.execify.model.mapper.CodeMapper;
import com.rce.execify.repository.CodeExecutorRepository;
import com.rce.execify.util.CheckedFunction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
public class DockerCodeExecutorService {

    private final CodeExecutorRepository codeExecutorRepository;

    public DockerCodeExecutorService(CodeExecutorRepository codeExecutorRepository) {
        this.codeExecutorRepository = codeExecutorRepository;
    }

    public Mono<CodeResponseDto> runJavaCode(CodeRequestDto codeRequestDto) {
        return Mono.just(codeRequestDto)
                .map(CodeMapper::toEntity)
                .flatMap(codeExecutorRepository::save)
                .map(CodeMapper::toDto)
                .map(CodeRequestDto::getCode)
                .map(CheckedFunction.unchecked(c -> this.createFile(c)))
                .flatMap(this::compileProcess)
                .flatMap(process -> {
                    return this.streamOutput(process)
                            .collect(Collectors.joining("\n"))
                            .map(CheckedFunction.unchecked(c -> {
                                int compilationResult = 0;
                                compilationResult = process.waitFor();
                                return compilationResult;
                            }))
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .filter(res -> res == 1)
                .then(this.executeProcess())
                .flatMap(process -> {
                    return this.streamOutput(process)
                            .collect(Collectors.joining("\n"))
                            .zipWhen(c -> {
                                return Mono.just(process)
                                        .map(CheckedFunction.unchecked(p -> {
                                            int compilationResult = 0;
                                            compilationResult = process.waitFor();
                                            return compilationResult;
                                        }))
                                        .subscribeOn(Schedulers.boundedElastic());
                            });
                })
                .map(c -> {
                    String output = c.getT1();
                    int executionResult = c.getT2();
                    CodeResponseDto codeResponseDto = new CodeResponseDto().setExitCode(executionResult).setOutput(output);
                    return codeResponseDto;
                });
    }

    private Flux<String> streamOutput(Process compile) {
        return Flux.fromStream(() -> new BufferedReader(new InputStreamReader(compile.getInputStream())).lines());
    }

    private Mono<Process> compileProcess(Path path) {
        return Mono.just(path)
                .map(c -> {
                    Process compileCode = null;
                    try {
                        var builder = new ProcessBuilder(
                                "docker", "run", "--rm",
                                "--memory=256m", "--cpus=0.5",
                                "--network", "none",
                                "-v", Paths.get("").toAbsolutePath() + ":/usr/src/app",
                                "-w", "/usr/src/app",
                                "eclipse-temurin:17-jdk",
                                "javac", "Solution.java"
                        );
                        builder.redirectErrorStream(true);
                        compileCode = builder.start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return compileCode;
                });
    }

    private Mono<Process> executeProcess() {
        return Mono.fromSupplier(() -> {
            Process executeCode = null;
            try {
                executeCode = new ProcessBuilder(
                        "docker", "run", "--rm",
                        "--memory=256m", "--cpus=0.5",
                        "--network", "none",
                        "-v", Paths.get("").toAbsolutePath() + ":/usr/src/app",
                        "-w", "/usr/src/app",
                        "eclipse-temurin:17-jdk",
                        "java", "Solution"
                ).redirectErrorStream(true).start();
            } catch (IOException e) {

            }
            return executeCode;
        });
    }

    private Path createFile(String code) throws IOException {
        Path currentDir = Paths.get("");
        Path absPathToProgramFile = currentDir.toAbsolutePath().resolve("Solution.java");
        if (!Files.exists(absPathToProgramFile)) Files.createFile(absPathToProgramFile);

        Files.writeString(absPathToProgramFile, code);
        return absPathToProgramFile;
    }
}
