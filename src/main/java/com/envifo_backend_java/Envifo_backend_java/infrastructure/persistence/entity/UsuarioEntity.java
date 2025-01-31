package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Column(name = "user_name")
    private String userName;

    private String nombre;

    private String apellido;

    private int edad;

    private String celular;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolesEntity rol ;



    // Constructores


    public UsuarioEntity(Long idUsuario, LocalDateTime fechaRegistro, boolean estado, String userName,
                         String nombre, String apellido, int edad, String celular, String email,
                         String password, String fechaNacimiento,
                         RolesEntity rol) {

        this.idUsuario = idUsuario;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.userName = userName;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.celular = celular;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
        this.rol = rol;

    }

    public UsuarioEntity(){

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public RolesEntity getRol() {
        return rol;
    }

    public void setRol(RolesEntity rol) {
        this.rol = rol;
    }

}
