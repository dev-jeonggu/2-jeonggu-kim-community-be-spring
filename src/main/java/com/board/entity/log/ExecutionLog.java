package com.board.entity.log;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "execution_log", schema = "community")
public class ExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private String methodName;
    private Long executionTimeMs;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime regDt;

    @PrePersist
    public void prePersist() {
        this.regDt = LocalDateTime.now();
    }

    public ExecutionLog() {}

    public ExecutionLog(String methodName, Long executionTimeMs, LocalDateTime startTime, LocalDateTime endTime) {
        this.methodName = methodName;
        this.executionTimeMs = executionTimeMs;
        this.startTime = startTime;
        this.endTime = endTime;
        this.regDt = LocalDateTime.now();
    }

}
