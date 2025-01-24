package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.service_exception.UserAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
   
   @ExceptionHandler(UserAlreadyExistsException.class)
   public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
   }

   @ExceptionHandler(RuntimeException.class)
   public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
   }
   @ExceptionHandler(Exception.class)
   public ResponseEntity<String> handleException(Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
   }


}
