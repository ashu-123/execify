package com.rce.execify.mapper;

import com.rce.execify.model.dto.CodeRequestDto;
import com.rce.execify.model.entity.Code;
import org.bson.types.ObjectId;

public class CodeMapper {

    public static Code toEntity(CodeRequestDto codeRequestDto) {
        return new Code().setId(new ObjectId()).setProgram(codeRequestDto.getCode());
    }

    public static CodeRequestDto toDto(Code code) {
        return new CodeRequestDto().setCode(code.getProgram()).setLanguage("java");
    }
}
