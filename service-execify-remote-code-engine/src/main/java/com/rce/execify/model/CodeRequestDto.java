package com.rce.execify.model;

import java.util.Objects;

public class CodeRequestDto {

    private String code;

    private String language;

    public String getCode() {
        return code;
    }

    public CodeRequestDto setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public CodeRequestDto setLanguage(String language) {
        this.language = language;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeRequestDto that = (CodeRequestDto) o;
        return Objects.equals(code, that.code) && Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, language);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CodeRequestDto{");
        sb.append("code='").append(code).append('\'');
        sb.append(", language='").append(language).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
