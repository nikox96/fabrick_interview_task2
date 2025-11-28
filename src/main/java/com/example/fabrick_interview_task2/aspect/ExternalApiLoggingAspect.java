package com.example.fabrick_interview_task2.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ExternalApiLoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.example.fabrick_interview_task2.client.impl..*(..))")
    public void externalApiClientMethods() {
    }

    @Around("externalApiClientMethods()")
    public Object logExternalApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("Outgoing API call with client {}.{} with params {}", className, methodName, formatArguments(args));

        Object response = null;
        Exception exception = null;

        try {
            response = joinPoint.proceed();
            return response;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Incoming API response in {} ms", duration);

            if (exception != null) {
                log.error("Response failed with {} message: {}", exception.getClass().getSimpleName(), exception.getMessage(), exception);
            } else {
                try {
                    String responseBody = objectMapper.writeValueAsString(response);
                    log.info("Response success with Body: {}", responseBody);
                } catch (Exception e) {
                    log.info("Response: {}", response);
                    log.warn("Unable to serialize response body: {}", e.getMessage(), e);
                }
            }
        }
    }

    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(args[i]);
        }
        sb.append("]");

        return sb.toString();
    }
}