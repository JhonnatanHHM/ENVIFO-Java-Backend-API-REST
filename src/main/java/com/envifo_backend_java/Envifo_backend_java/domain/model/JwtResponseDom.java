package com.envifo_backend_java.Envifo_backend_java.domain.model;

public class JwtResponseDom {

    private String accessToken;
    public JwtResponseDom(String accessToken){
        this.accessToken = accessToken;
    }

    public JwtResponseDom(){}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
