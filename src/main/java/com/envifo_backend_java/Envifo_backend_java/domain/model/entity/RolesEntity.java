package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @JsonIgnore
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClienteUsuarioRolEntity> usuarioRoles = new HashSet<>();

    // Constructor

    public RolesEntity(Long idRol, String name, String description, PermisosEntity permisos, Set<ClienteUsuarioRolEntity> usuarioRoles) {
        this.idRol = idRol;
        this.name = name;
        this.description = description;
        this.permisos = permisos;
        this.usuarioRoles = usuarioRoles;
    }

    public RolesEntity() {
    }

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

    public Set<ClienteUsuarioRolEntity> getUsuarioRoles() {
        return usuarioRoles;
    }

    public void setUsuarioRoles(Set<ClienteUsuarioRolEntity> usuarioRoles) {
        this.usuarioRoles = usuarioRoles;
    }

}
