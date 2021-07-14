package com.anderson.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseExceptionDetails> notFound(NotFoundException erro){
        ResponseExceptionDetails responseErro = new ResponseExceptionDetails(new Date(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), erro.getMessage());
        return new ResponseEntity<>(responseErro, HttpStatus.NOT_FOUND);
    }
}
