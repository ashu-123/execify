package com.rce.execify.model.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("code")
public class Code {

    @Id
    private ObjectId id;

    private String program;

    public ObjectId getId() {
        return id;
    }

    public Code setId(ObjectId id) {
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
