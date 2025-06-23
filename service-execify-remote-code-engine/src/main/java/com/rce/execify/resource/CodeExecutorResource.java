package com.rce.execify.resource;

import com.rce.execify.model.dto.CodeRequestDto;
import com.rce.execify.model.dto.CodeResponseDto;
import com.rce.execify.service.CodeExecutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code")
@CrossOrigin
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
