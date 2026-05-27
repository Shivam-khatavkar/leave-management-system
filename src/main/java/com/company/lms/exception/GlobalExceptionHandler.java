package com.company.lms.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));

        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        
    }

    
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
    	
    

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 404);

        error.put("error","NOT_FOUND");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
        
    }

    
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(DuplicateEmailException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 409);

        error.put("error","CONFLICT");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
        
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 500);

        error.put("error","INTERNAL_SERVER_ERROR");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }
    
    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLeaveNotFoundException(LeaveNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 404);

        error.put("error","NOT_FOUND");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidLeaveDateException.class)
    public ResponseEntity<Map<String, Object>>
    handleInvalidLeaveDateException(
            InvalidLeaveDateException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 400);

        error.put("error","BAD_REQUEST");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        
    }
    
    @ExceptionHandler(LeaveOverlapException.class)
    public ResponseEntity<Map<String, Object>>
    handleLeaveOverlapException(
            LeaveOverlapException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 409);

        error.put("error","CONFLICT");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
        
    }
    
    @ExceptionHandler(InsufficientLeaveBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientLeaveBalanceException(InsufficientLeaveBalanceException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp",LocalDateTime.now());

        error.put("status", 400);

        error.put("error","BAD_REQUEST");

        error.put("message",ex.getMessage());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        
    }
    
   
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {

        return new ResponseEntity<>("Invalid username or password",HttpStatus.UNAUTHORIZED);
        
    }
    
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String>handleJwtException(JwtException ex) {

        return new ResponseEntity<>("Invalid or expired token",HttpStatus.UNAUTHORIZED);
        
    }
    
   
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", 403);
        error.put("error", "FORBIDDEN");
        error.put("message", ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
}