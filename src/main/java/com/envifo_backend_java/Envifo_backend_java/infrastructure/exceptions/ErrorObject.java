package com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions;


import java.util.Date;

public class ErrorObject {
    private Integer statusCode;
    private String message;
    private Date timestamp;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}