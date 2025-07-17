package com.demo.demo.exception;

import com.demo.demo.exception.exceptions.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {

    // mục tiêu: bắt lỗi và return message về cho phía FE

    // MethodArgumentNotValidException

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleBadRequestException(MethodArgumentNotValidException exception){
        System.out.println("Người dùng nhập chưa đúng thông tin");
        String responseMessage = "";

        for(FieldError fieldError: exception.getFieldErrors()){
            responseMessage += fieldError.getDefaultMessage() + "\n";
        }

        return new ResponseEntity<String>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException exception){


        return new ResponseEntity<String>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}


// ORM: Object Relationship Mapping
// Code first