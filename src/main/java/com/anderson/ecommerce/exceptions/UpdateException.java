package com.anderson.ecommerce.exceptions;

public class UpdateException extends RuntimeException{
    public UpdateException(String classe, String mensagem){
        super(classe + " não pode ser atualizado: " + mensagem);
    }
}
