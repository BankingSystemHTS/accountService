package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.service_exception.ResourceNotFoundException;
import com.example.exception.service_exception.UserAlreadyExistsException;
import com.example.exception.service_exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
   
   @ExceptionHandler(UserAlreadyExistsException.class)
   public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
   }

   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
   }
   
   @ExceptionHandler(ResourceNotFoundException.class)
   public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
      return ResponseEntity.noContent().build();
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
