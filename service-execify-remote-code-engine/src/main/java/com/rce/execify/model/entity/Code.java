package com.rce.execify.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String program;

    public Long getId() {
        return id;
    }

    public Code setId(Long id) {
        this.id = id;
        return this;
    }

    public String getProgram() {
        return program;
    }

    public Code setProgram(String program) {
        this.program = program;
        return this;
    }
}
