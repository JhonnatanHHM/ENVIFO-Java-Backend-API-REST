package com.envifo_backend_java.Envifo_backend_java.application.dto;

public class CategoriesDto {

    private Long idCategoria;
    private String nombre;
    private String section;
    private Boolean estado;
    private Long idCliente;

    public CategoriesDto() {}

    public CategoriesDto(Long idCategoria, String nombre, String section, Boolean estado, Long idCliente) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.section = section;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    // Getters y setters

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
}
