package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.math.BigDecimal;
import java.util.Optional;

public class MaterialCompleteDto {

    private Long idMaterial;
    private String nameMaterial;
    private String descripcionMate;
    private BigDecimal height;
    private BigDecimal width;
    private boolean status;
    private Long idCategoria;
    private String nameCategory;
    private TextureCompleteDto texture;
    private Optional<StorageDto> material;

    public MaterialCompleteDto() {
    }

    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNameMaterial() {
        return nameMaterial;
    }

    public void setNameMaterial(String nameMaterial) {
        this.nameMaterial = nameMaterial;
    }

    public String getDescripcionMate() {
        return descripcionMate;
    }

    public void setDescripcionMate(String descripcionMate) {
        this.descripcionMate = descripcionMate;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public TextureCompleteDto getTexture() {
        return texture;
    }

    public void setTexture(TextureCompleteDto texture) {
        this.texture = texture;
    }

    public Optional<StorageDto> getMaterial() {
        return material;
    }

    public void setMaterial(Optional<StorageDto> material) {
        this.material = material;
    }
}
