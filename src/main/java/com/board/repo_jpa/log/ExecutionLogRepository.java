package com.board.repo_jpa.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.board.entity.log.ExecutionLog;

@Repository
public interface ExecutionLogRepository extends JpaRepository<ExecutionLog, Long> {
}
