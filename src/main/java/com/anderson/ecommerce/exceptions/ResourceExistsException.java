package com.anderson.ecommerce.exceptions;

public class ResourceExistsException extends RuntimeException{

    public ResourceExistsException(String message){
        super(message);
    }
}
