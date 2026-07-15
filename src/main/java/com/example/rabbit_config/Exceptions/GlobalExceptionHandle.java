package com.example.rabbit_config.Exceptions;

import java.nio.file.AccessDeniedException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

    // For postgres
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDuplicateEmail(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Email already exists, try a new one !");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid email or password");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User not found");
    }

    public class RetryableGeminiException extends RuntimeException {
        public RetryableGeminiException(String message) {
            super(message);
        }
    }

    public class PermanentGeminiException extends RuntimeException {
        public PermanentGeminiException(String message) {
            super(message);
        }
    }

    // @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    // public ResponseEntity<String> handleJWTExpiedError() {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expired.
    // Please login again");
    // }

    // @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    // public ResponseEntity<String> handleJwtError(io.jsonwebtoken.JwtException ex)
    // {
    // return ResponseEntity
    // .status(HttpStatus.UNAUTHORIZED)
    // .body("Invalid JWT token");
    // }

}
