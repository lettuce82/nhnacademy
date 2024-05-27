package com.nhnacademy.nhnmart.aop;

import com.nhnacademy.nhnmart.db.DbConnectionThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class DbConnectionAspect {
    private final DbConnectionThreadLocal dbConnectionThreadLocal;

    public DbConnectionAspect(DbConnectionThreadLocal dbConnectionThreadLocal) {
        this.dbConnectionThreadLocal = dbConnectionThreadLocal;
    }

    @Before("execution(* com.nhnacademy.nhnmart.repository.*.*.*(..))")
    public void initializeConnection() {
        dbConnectionThreadLocal.initialize();
    }

    @AfterReturning("execution(* com.nhnacademy.nhnmart.repository.*.*.*(..))")
    public void resetConnection() {
        dbConnectionThreadLocal.reset();
    }

    @AfterThrowing("execution(* com.nhnacademy.nhnmart.repository.*.*.*(..))")
    public void resetConnectionOnException() {
        dbConnectionThreadLocal.reset();
    }
}