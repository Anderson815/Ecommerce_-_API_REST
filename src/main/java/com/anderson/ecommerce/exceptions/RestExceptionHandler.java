package com.anderson.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseExceptionDetails> notFound(NotFoundException erro){
        ResponseExceptionDetails responseErro = new ResponseExceptionDetails(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), erro.getMessage());
        return new ResponseEntity<>(responseErro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<ResponseExceptionDetails> create(InvalidValueException erro){
        ResponseExceptionDetails responseErro = new ResponseExceptionDetails(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), erro.getMessage());
        return ResponseEntity.badRequest().body(responseErro);
    }

    @ExceptionHandler(UpdateException.class)
    public ResponseEntity<ResponseExceptionDetails> update(UpdateException erro){
        ResponseExceptionDetails responseErro = new ResponseExceptionDetails(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), erro.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErro);
    }
}
