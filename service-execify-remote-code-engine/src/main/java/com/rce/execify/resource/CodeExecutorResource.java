package com.rce.execify.resource;

import com.rce.execify.model.CodeRequestDto;
import com.rce.execify.model.CodeResponseDto;
import com.rce.execify.service.CodeExecutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class CodeExecutorResource {

    private final CodeExecutorService codeExecutorService;

    public CodeExecutorResource(CodeExecutorService codeExecutorService) {
        this.codeExecutorService = codeExecutorService;
    }

    @PostMapping("/execute")
    public ResponseEntity<CodeResponseDto> process(@RequestBody CodeRequestDto codeRequestDto) {
        var codeResponse = codeExecutorService.process(codeRequestDto);
        return ResponseEntity.ok(codeResponse);
    }


}
