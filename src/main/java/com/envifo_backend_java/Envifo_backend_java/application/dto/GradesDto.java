package com.envifo_backend_java.Envifo_backend_java.application.dto;

public class GradesDto {

    //Atributos

    private Long idGrade;

    private String title;

    private String content;

    private Long idUser;

    private Long idCustomer;

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

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }
}
