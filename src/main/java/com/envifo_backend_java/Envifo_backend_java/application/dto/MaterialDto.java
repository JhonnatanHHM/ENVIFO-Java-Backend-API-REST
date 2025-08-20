package com.envifo_backend_java.Envifo_backend_java.application.dto;

import java.math.BigDecimal;

public class MaterialDto {

    private Long idMaterial;
    private String nameMaterial;
    private String descripcionMate;
    private BigDecimal height;
    private BigDecimal width;
    private boolean status;
    private Long idCategoria;
    private Long idTextura;

    public MaterialDto() {
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

    public Long getIdTextura() {
        return idTextura;
    }

    public void setIdTextura(Long idTextura) {
        this.idTextura = idTextura;
    }
}
