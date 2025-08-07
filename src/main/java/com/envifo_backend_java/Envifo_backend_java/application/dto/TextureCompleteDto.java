package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.util.Optional;

public class TextureCompleteDto {

    private Long idTexture;

    private String nameTexture;

    private String description;

    private Optional<StorageDto> image;

    public TextureCompleteDto() {
    }

    public TextureCompleteDto(Long idTexture, String nameTexture, String description, Optional<StorageDto> image) {
        this.idTexture = idTexture;
        this.nameTexture = nameTexture;
        this.description = description;
        this.image = image;
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

    public Optional<StorageDto> getImage() {
        return image;
    }

    public void setImage(Optional<StorageDto> image) {
        this.image = image;
    }
}
