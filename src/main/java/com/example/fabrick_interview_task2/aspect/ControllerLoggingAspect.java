package com.example.fabrick_interview_task2.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerLoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {
    }

    @Around("restControllerMethods()")
    public Object logIncomingRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String fullUrl = queryString != null ? uri + "?" + queryString : uri;

        Map<String, String> headers = getRequestHeaders(request);
        Object[] args = joinPoint.getArgs();

        log.info("Incoming request {} received at: {} with headers: {} with params: {}", method, fullUrl, headers, Arrays.toString(args));

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
            log.info("Outgoing response in {}", duration);

            if (exception != null) {
                log.error("Response failed with exception message: {}", exception.getMessage(), exception);
            } else {
                if (response instanceof ResponseEntity<?> responseEntity) {
                    log.info("Status: {}", responseEntity.getStatusCode());
                    try {
                        String responseBody = objectMapper.writeValueAsString(responseEntity.getBody());
                        log.info("Response Body: {}", responseBody);
                    } catch (Exception e) {
                        log.warn("Unable to serialize response body; Http status {}", responseEntity.getStatusCode(), e);
                    }
                } else {
                    log.info("Response: {}", response);
                }
            }
        }
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return headers;
    }
}