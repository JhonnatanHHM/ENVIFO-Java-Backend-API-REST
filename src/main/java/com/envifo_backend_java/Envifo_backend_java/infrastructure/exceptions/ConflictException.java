package com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions;

public class ConflictException extends RuntimeException{

    private static final long serialVersionUID = 1;

    public ConflictException(String message){
        super(message);
    }
}

//Esta clase maneja los conflictos para cuando ya extiste un registro creado