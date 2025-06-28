package com.rce.execify.resource;

import com.rce.execify.model.dto.CodeRequestDto;
import com.rce.execify.model.dto.CodeResponseDto;
import com.rce.execify.service.DockerCodeExecutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/code")
@CrossOrigin
public class CodeExecutorResource {

    private final DockerCodeExecutorService dockerCodeExecutorService;

    public CodeExecutorResource(DockerCodeExecutorService dockerCodeExecutorService) {
        this.dockerCodeExecutorService = dockerCodeExecutorService;
    }

    @PostMapping("/execute")
    public Mono<ResponseEntity<CodeResponseDto>> process(@RequestBody CodeRequestDto codeRequestDto) {
        return Mono.just(codeRequestDto)
                .flatMap(dockerCodeExecutorService::runJavaCode)
                .map(ResponseEntity::ok);
    }

}
