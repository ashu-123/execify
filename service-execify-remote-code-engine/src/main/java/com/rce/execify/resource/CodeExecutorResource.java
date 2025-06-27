package com.rce.execify.resource;

import com.rce.execify.model.dto.CodeRequestDto;
import com.rce.execify.model.dto.CodeResponseDto;
import com.rce.execify.service.CodeExecutorService;
import com.rce.execify.service.DockerCodeExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/code")
@CrossOrigin
public class CodeExecutorResource {

    private final CodeExecutorService codeExecutorService;

    private final DockerCodeExecutor dockerCodeExecutor;

    public CodeExecutorResource(CodeExecutorService codeExecutorService,
                                DockerCodeExecutor dockerCodeExecutor) {
        this.codeExecutorService = codeExecutorService;
        this.dockerCodeExecutor = dockerCodeExecutor;
    }

    @PostMapping("/execute")
    public ResponseEntity<CodeResponseDto> process(@RequestBody CodeRequestDto codeRequestDto) throws IOException, InterruptedException {
        CodeResponseDto codeResponseDto = dockerCodeExecutor.runJavaCode(codeRequestDto);
        return ResponseEntity.ok(codeResponseDto);
    }


}
