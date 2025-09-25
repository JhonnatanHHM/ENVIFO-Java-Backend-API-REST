package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.time.LocalDateTime;

public class GradesDto {

    //Atributos

    private Long idGrade;

    private String title;

    private LocalDateTime updatedDate;

    private String content;

    private Long idUser;

    private Long idCustomer;

    public GradesDto(Long idGrade, String title, LocalDateTime updatedDate, String content, Long idUser, Long idCustomer) {
        this.idGrade = idGrade;
        this.title = title;
        this.updatedDate = updatedDate;
        this.content = content;
        this.idUser = idUser;
        this.idCustomer = idCustomer;
    }

    public GradesDto() {
    }

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

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {

        this.idUser = idUser;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }
}
