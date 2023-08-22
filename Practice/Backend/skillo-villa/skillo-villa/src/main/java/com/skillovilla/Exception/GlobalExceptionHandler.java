package com.skillovilla.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDetails> customExceptionHandler(CustomException e){
        ErrorDetails err = new ErrorDetails(LocalDate.now(),e.getMessage());

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
