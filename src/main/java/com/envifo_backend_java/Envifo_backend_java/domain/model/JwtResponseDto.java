package com.envifo_backend_java.Envifo_backend_java.domain.model;

public class JwtResponseDto {

    private String accessToken;
    public JwtResponseDto(String accessToken){
        this.accessToken = accessToken;
    }

    public JwtResponseDto(){}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
