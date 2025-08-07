package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import com.envifo_backend_java.Envifo_backend_java.application.dto.UserCompleteDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


@Entity
@Table(name = "usuarios")
@ToString
@EqualsAndHashCode
public class UsuarioEntity {

//Atributos

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false, nullable = false)
    private LocalDateTime fechaRegistro;

    private boolean estado;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "primer_nombre", length = 50, nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @Column(name = "primer_apellido", length = 50, nullable = false)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;

    private String edad;

    private String celular;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolesEntity rol;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClienteUsuarioRolEntity> clienteRoles = new HashSet<>();

    // Constructor

    public UsuarioEntity(Long idUsuario, LocalDateTime fechaRegistro, boolean estado, String userName, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String edad, String celular, String email, String password, RolesEntity rol, Set<ClienteUsuarioRolEntity> clienteRoles) {
        this.idUsuario = idUsuario;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.userName = userName;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.edad = edad;
        this.celular = celular;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.clienteRoles = clienteRoles;
    }

    public UsuarioEntity() {
    }

// Getters and Setters


    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
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



    public RolesEntity getRol() {
        return rol;
    }

    public void setRol(RolesEntity rol) {
        this.rol = rol;
    }

    public Set<ClienteUsuarioRolEntity> getClienteRoles() {
        return clienteRoles;
    }

    public void setClienteRoles(Set<ClienteUsuarioRolEntity> clienteRoles) {
        this.clienteRoles = clienteRoles;
    }
}
