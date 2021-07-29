package com.anderson.ecommerce.exceptions;

public class InvalidValueException extends RuntimeException{

    public InvalidValueException(String classe, String mensagem){
        super(classe + " não pode ser criado: " + mensagem);
    }
}
