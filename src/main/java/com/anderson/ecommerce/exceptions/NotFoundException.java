package com.anderson.ecommerce.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String classe, String id){
        super("Não existe " + classe + " de id: " + id);
    }

}
