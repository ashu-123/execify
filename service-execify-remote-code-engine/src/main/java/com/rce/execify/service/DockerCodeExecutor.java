package com.rce.execify.service;

import com.rce.execify.model.dto.CodeRequestDto;
import com.rce.execify.model.dto.CodeResponseDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
public class DockerCodeExecutor {

    public CodeResponseDto runJavaCode(CodeRequestDto codeRequestDto) throws IOException, InterruptedException {
        String code = codeRequestDto.getCode();
        CodeResponseDto codeResponseDto = null;

        Path currentDir = Paths.get("");
        Path absPathToProgramFile = currentDir.toAbsolutePath().resolve("Solution.java");
        if (!Files.exists(absPathToProgramFile)) Files.createFile(absPathToProgramFile);

        Files.writeString(absPathToProgramFile, code);

        Process compileCode = new ProcessBuilder(
                "docker", "run", "--rm",
                "--memory=256m", "--cpus=0.5",
                "--network", "none",
                "-v", currentDir.toAbsolutePath() + ":/usr/src/app",
                "-w", "/usr/src/app",
                "eclipse-temurin:17-jdk",
                "javac", "Solution.java"
        ).start();

        String compilationOutput = new BufferedReader(new InputStreamReader(compileCode.getInputStream()))
                .lines()
                .collect(Collectors.joining("\n"));
        String compilationErrors = new BufferedReader(new InputStreamReader(compileCode.getErrorStream()))
                .lines()
                .collect(Collectors.joining("\n"));

        int compilationResult = compileCode.waitFor();

        if (compilationResult == 0) {
            Process executeCode = new ProcessBuilder(
                    "docker", "run", "--rm",
                    "--memory=256m", "--cpus=0.5",
                    "--network", "none",
                    "-v", currentDir.toAbsolutePath() + ":/usr/src/app",
                    "-w", "/usr/src/app",
                    "eclipse-temurin:17-jdk",
                    "java", "Solution"
            ).start();

            String executionOutput = new BufferedReader(new InputStreamReader(executeCode.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            String executionErrors = new BufferedReader(new InputStreamReader(executeCode.getErrorStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

            int executionResult = executeCode.waitFor();
            if (executionResult == 0) codeResponseDto = new CodeResponseDto().setExitCode(0).setOutput(executionOutput);
            else codeResponseDto = new CodeResponseDto().setExitCode(1).setError(executionErrors);
        } else {
            codeResponseDto = new CodeResponseDto().setExitCode(1).setError(compilationErrors);
        }

        return codeResponseDto;
    }
}
