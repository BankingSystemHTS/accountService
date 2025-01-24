package com.example.exception.service_exception;

public class UserAlreadyExistsException extends RuntimeException{
   public UserAlreadyExistsException(String message) {
      super(message);
   }
}
