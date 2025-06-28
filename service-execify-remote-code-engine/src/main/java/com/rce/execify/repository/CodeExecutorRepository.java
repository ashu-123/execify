package com.rce.execify.repository;

import com.rce.execify.model.entity.Code;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface CodeExecutorRepository extends ReactiveMongoRepository<Code, ObjectId> {
}
