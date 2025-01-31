package com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions;

public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1;

    public NotFoundException(String message){
        super(message);
    }
}

//Exception para cuando no se encuentre un recurso