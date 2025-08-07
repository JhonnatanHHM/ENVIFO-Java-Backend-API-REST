package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clientes")
public class ClientesEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String telefono;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String url;

    private boolean estado;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false, nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolesEntity rol;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClienteUsuarioRolEntity> usuarioRoles = new HashSet<>();

    // Constructor

    public ClientesEntity(Long idCliente, String nombre, String direccion, String telefono, String email, String password, String url, boolean estado, LocalDateTime fechaRegistro, RolesEntity rol, Set<ClienteUsuarioRolEntity> usuarioRoles) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.url = url;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
        this.usuarioRoles = usuarioRoles;
    }

    public ClientesEntity() {
    }

    // Getters and Setters

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public RolesEntity getRol() {
        return rol;
    }

    public void setRol(RolesEntity rol) {
        this.rol = rol;
    }

    public Set<ClienteUsuarioRolEntity> getUsuarioRoles() {
        return usuarioRoles;
    }

    public void setUsuarioRoles(Set<ClienteUsuarioRolEntity> usuarioRoles) {
        this.usuarioRoles = usuarioRoles;
    }

}
