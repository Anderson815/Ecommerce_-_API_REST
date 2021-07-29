package com.anderson.ecommerce.exceptions;

public class InvalidValueException extends RuntimeException{

    public InvalidValueException(String mensagem){
        super("Campo inválido: " + mensagem);
    }
}
