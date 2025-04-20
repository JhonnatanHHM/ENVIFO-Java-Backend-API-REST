package com.envifo_backend_java.Envifo_backend_java.application.dto;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;


public class GradesDto {

    //Atributos

    private Long idGrade;

    private String title;

    private String content;

    private Long idUser;

    // Getters and Setters


    public Long getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(Long idGrade) {
        this.idGrade = idGrade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
