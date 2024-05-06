package com.example.financetracker.component;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Getter
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final RequestCounter requestCounter;

    public LoggingAspect(RequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

    @Pointcut("execution(* com.example.financetracker.controller.TrackerController.create*(..))")
    public void create() {
    }

    @Pointcut("execution(* com.example.financetracker.controller.TrackerController.get*(..))")
    public void getEntity() {
    }

    @Pointcut("execution(* com.example.financetracker.controller.TrackerController.update*(..))")
    public void update() {
    }

    @Pointcut("execution(* com.example.financetracker.controller.TrackerController.delete*(..))")
    public void delete() {
    }

    @Pointcut("execution(* com.example.financetracker.exceptions.*.handleInternalServerError*(..))")
    public void handle() {
    }

    @AfterReturning(pointcut = "create()", returning = "result")
    public void logCreate(Object result) {
        logger.info("Created: {}", result);
    }

    @AfterReturning(pointcut = "update()", returning = "result")
    public void logUpdate(Object result) {
        logger.info("Updated: {}", result);
    }

    @AfterReturning(pointcut = "delete()", returning = "result")
    public void logDelete(Object result) {
        logger.info("Deleted: {}", result);
    }

    @After("create() || getEntity() || update() || delete() || handle()")
    public void logRequest() {
        requestCounter.incrementAndGet();
    }

    @PreDestroy
    public void logRequestCount() {
        logger.info("Total number of requests: {}", requestCounter.getRequestCount());
    }
}