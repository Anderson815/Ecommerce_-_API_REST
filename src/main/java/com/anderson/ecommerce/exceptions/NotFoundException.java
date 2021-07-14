package com.anderson.ecommerce.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String id){
        super("Não existe cliente de id: " + id);
    }

}
