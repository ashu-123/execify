package com.rce.execify.model;

public class CodeInputRequestDto {

    private String code;

    private String language;

    public String getCode() {
        return code;
    }

    public CodeInputRequestDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public CodeInputRequestDto setLanguage(String language) {
        this.language = language;
        return this;
    }
}
