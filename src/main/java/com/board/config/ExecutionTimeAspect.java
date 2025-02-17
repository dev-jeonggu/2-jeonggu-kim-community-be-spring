package com.board.config;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.board.entity.log.ExecutionLog;
import com.board.repo_jpa.log.ExecutionLogRepository;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Autowired
    private ExecutionLogRepository executionLogRepository;
    
    @Around("execution(* com.board.service..*(..))") 
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        ExecutionLog log = new ExecutionLog();
        log.setMethodName(joinPoint.getSignature().toString());
        log.setExecutionTimeMs(executionTime);
        log.setStartTime(LocalDateTime.now());
        log.setEndTime(LocalDateTime.now());

        executionLogRepository.save(log);

        System.out.println("[Execution Time] " + joinPoint.getSignature() + " 실행 시간: " + executionTime + "ms (DB 저장 완료)");
        return proceed;
    }
}