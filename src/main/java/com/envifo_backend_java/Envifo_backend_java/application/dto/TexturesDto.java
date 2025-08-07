package com.envifo_backend_java.Envifo_backend_java.application.dto;

public class TexturesDto {

    private Long idTexture;

    private String nameTexture;

    private String description;

    private Long idCategory;

    public TexturesDto() {
    }

    public Long getIdTexture() {
        return idTexture;
    }

    public void setIdTexture(Long idTexture) {
        this.idTexture = idTexture;
    }

    public String getNameTexture() {
        return nameTexture;
    }

    public void setNameTexture(String nameTexture) {
        this.nameTexture = nameTexture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
}
