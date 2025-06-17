package com.rce.execify.service;

import com.rce.execify.model.CodeRequestDto;
import com.rce.execify.model.CodeResponseDto;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class CodeExecutorService {

    public CodeResponseDto process(CodeRequestDto codeRequestDto) {

        CodeResponseDto codeResponseDto = null;

        try {

            File inputFile = createInputFile(codeRequestDto.getCode());

            Process compile = compile(inputFile.getName());
            int compilationResult = compile.waitFor();
            if (compilationResult != 0) {
                System.err.println("Compilation failed:");
                String compilationError = printStream(compile.getErrorStream());

                codeResponseDto = new CodeResponseDto();
                codeResponseDto.setExitCode(compilationResult)
                        .setError(compilationError);

                return codeResponseDto;
            }

            String className = inputFile.getName().split("\\.")[0];
            Process run = run(className);
            String runtimeOutput = printStream(run.getInputStream());
            int runResult = run.waitFor();
            System.out.println("Program exited with code: " + runResult);

            codeResponseDto = new CodeResponseDto();
            codeResponseDto.setOutput(runtimeOutput)
                    .setExitCode(0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return codeResponseDto;
    }

    private static File createInputFile(String code) throws IOException {
        File sourceFile = new File("MyProgram.java");
        try (FileWriter writer = new FileWriter(sourceFile)) {
            writer.write(code);
        }

        return sourceFile;
    }

    private static Process compile(String inputFileName) throws IOException, InterruptedException {
        ProcessBuilder compile = new ProcessBuilder("javac", inputFileName);
        return compile.start();
    }

    private static Process run(String className) throws IOException, InterruptedException {
        ProcessBuilder run = new ProcessBuilder("java", className);
        run.redirectErrorStream(true); // Combine stderr with stdout
        return run.start();
    }

    private static String printStream(InputStream inputStream) throws IOException {
        StringBuilder processOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                processOutput.append(line);
            }
        }

        return processOutput.toString();
    }
}
