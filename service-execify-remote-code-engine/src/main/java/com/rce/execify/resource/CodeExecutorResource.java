package com.rce.execify.resource;

import com.rce.execify.model.CodeRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/code")
public class CodeExecutorResource {

    @PostMapping("/execute")
    public ResponseEntity<String> executeCode(@RequestBody CodeRequestDto codeRequestDto) {

        try {
            // Step 1: Write the code to a file
            File sourceFile = new File("MyProgram.java");
            try (FileWriter writer = new FileWriter(sourceFile)) {
                writer.write(codeRequestDto.getCode());
            }

            // Step 2: Compile the Java program
            ProcessBuilder compileBuilder = new ProcessBuilder("javac", "MyProgram.java");
            Process compileProcess = compileBuilder.start();
            int compileResult = compileProcess.waitFor();

            if (compileResult != 0) {
                // Compilation failed — print error
                System.err.println("Compilation failed:");
                printStream(compileProcess.getErrorStream());
//                return;
            }

            // Step 3: Run the compiled class
            ProcessBuilder runBuilder = new ProcessBuilder("java", "MyProgram");
            runBuilder.redirectErrorStream(true); // Combine stderr with stdout
            Process runProcess = runBuilder.start();

            // Step 4: Print the output
            printStream(runProcess.getInputStream());

            int runResult = runProcess.waitFor();
            System.out.println("Program exited with code: " + runResult);

            return ResponseEntity.ok("Ran successfully :" + runResult);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("Failed");

    }

    private static void printStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
