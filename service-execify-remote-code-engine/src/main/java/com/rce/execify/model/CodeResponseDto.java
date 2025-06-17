package com.rce.execify.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

public class CodeResponseDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String output;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    private int exitCode;

    public String getOutput() {
        return output;
    }

    public CodeResponseDto setOutput(String output) {
        this.output = output;
        return this;
    }

    public String getError() {
        return error;
    }

    public CodeResponseDto setError(String error) {
        this.error = error;
        return this;
    }

    public int getExitCode() {
        return exitCode;
    }

    public CodeResponseDto setExitCode(int exitCode) {
        this.exitCode = exitCode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeResponseDto that = (CodeResponseDto) o;
        return exitCode == that.exitCode && Objects.equals(output, that.output) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, error, exitCode);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CodeResponseDto{");
        sb.append("output='").append(output).append('\'');
        sb.append(", error='").append(error).append('\'');
        sb.append(", exitCode=").append(exitCode);
        sb.append('}');
        return sb.toString();
    }
}
