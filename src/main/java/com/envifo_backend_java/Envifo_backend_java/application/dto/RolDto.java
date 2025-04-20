package com.envifo_backend_java.Envifo_backend_java.application.dto;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;

public class RolDto {

    private Long idRol;

    private String name;

    private String description;

    private PermisosEntity permisos ;

    // Getters and Setters


    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PermisosEntity getPermisos() {
        return permisos;
    }

    public void setPermisos(PermisosEntity permisos) {
        this.permisos = permisos;
    }
}
