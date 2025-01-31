package com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions;



import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorObject> handlerNotFoundException(ChangeSetPersister.NotFoundException ex){
        ErrorObject errorObject = new ErrorObject();

        //asignaciones de valores a los atributos de NotFoundException por medio del ErrorObject

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value()); //codigo HTTP
        errorObject.setMessage(ex.getMessage()); //mensaje
        errorObject.setTimestamp(new Date()); //fecha

        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorObject> handlerConflictException(ConflictException ex){
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.CONFLICT.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorObject> handlerAuthenticationCredentialsNotFoundException(JwtAuthenticationException ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handlerInternalServer(Exception ex){

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<ErrorObject>(errorObject,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}