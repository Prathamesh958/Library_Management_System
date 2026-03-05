package com.lms.exc_handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lms.custom_exceptions.APIException;
import com.lms.custom_exceptions.BookNotAvailableException;
import com.lms.custom_exceptions.ResourceNotFoundException;
import com.lms.dtos.ErrorDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex) {
      
        ErrorDetails err = new ErrorDetails(
            LocalDateTime.now(), 
            ex.getMessage(), 
            "The requested resource was not found", 
            404
        );
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ErrorDetails> handleBookAvailable(BookNotAvailableException ex) {
        ErrorDetails err = new ErrorDetails(
            LocalDateTime.now(), 
            ex.getMessage(), 
            "Inventory Check Failed", 
            400
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDetails> handleAPIException(APIException ex) {
        ErrorDetails err = new ErrorDetails(
            LocalDateTime.now(), 
            ex.getMessage(), 
            "Business Logic Error", 
            400
        );
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobal(Exception ex) {
        ErrorDetails err = new ErrorDetails(
            LocalDateTime.now(), 
            "Internal Server Error", 
            ex.getMessage(), 
            500
        );
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
}