package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
public class CategoriasEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 50)
    private String seccion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private Boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClientesEntity cliente;

    // Constructor


    public CategoriasEntity(Long idCategoria, String nombre, String seccion, LocalDateTime fechaCreacion, Boolean estado, ClientesEntity cliente) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.seccion = seccion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.cliente = cliente;
    }

    public CategoriasEntity() {
    }

    // Getters y Setters


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

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public ClientesEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClientesEntity cliente) {
        this.cliente = cliente;
    }
}