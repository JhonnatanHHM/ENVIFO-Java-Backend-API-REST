package com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

public class JwtAuthenticationException extends AuthenticationCredentialsNotFoundException {
    //private static final long serialVersionUID = 2;

    public JwtAuthenticationException(String message){
        super(message);
    }
}

//Exception para cuando hayan errores de credenciales