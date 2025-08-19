package com.springboot.lococo.aiplanner.config;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleBadRequest(
            IllegalArgumentException e, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(Map.of(
                "path", req.getRequestURI(),
                "error", e.getMessage()
        ));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Map<String,Object>> handleUpstream(
            HttpStatusCodeException e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                "path", req.getRequestURI(),
                "status", e.getStatusCode().value(),
                "error", "Gemini API error",
                "body", e.getResponseBodyAsString()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(
            Exception e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "path", req.getRequestURI(),
                "type", e.getClass().getSimpleName(),
                "message", e.getMessage()
        ));
    }
}