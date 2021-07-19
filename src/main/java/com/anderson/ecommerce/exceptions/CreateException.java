package com.anderson.ecommerce.exceptions;

public class CreateException extends RuntimeException{

    public CreateException(String classe, String mensagem){
        super(classe + " não pode ser criado: " + mensagem);
    }
}
