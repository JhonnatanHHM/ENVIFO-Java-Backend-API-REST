package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="roles")
public class RolesEntity {

    //Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(name = "nombre",unique = true)
    private String name;

    @Column(name = "descripcion")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_permiso")
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
