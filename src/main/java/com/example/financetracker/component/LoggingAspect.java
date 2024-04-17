package com.example.financetracker.component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.financetracker.service.TrackerService.*(..))")
    public void logServiceMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Method {} called.", methodName);
    }

    @AfterThrowing(pointcut = "execution(* com.example.financetracker.service.TrackerService.*(..))", throwing = "ex")
    public void logServiceMethodException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        LOGGER.error("Exception in method {}: {}", methodName, ex.getMessage());
    }
}
