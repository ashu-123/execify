package com.rce.execify.repository;

import com.rce.execify.model.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeExecutorRepository extends JpaRepository<Code, Long> {
}
