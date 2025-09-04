package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
public class ProyectosEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto", nullable = false)
    private Long idProyecto;

    @Column(name = "nombre_proyecto", length = 100, nullable = false)
    private String nombreProyecto;

    @Column(columnDefinition = "text")
    private String descripcion;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private LocalDateTime fechaCreacion;

    @CreationTimestamp
    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @Column(nullable = false)
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClientesEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_disenio", nullable = false)
    private Disenios3dEntity disenio;

    // Constructor

    public ProyectosEntity(Long idProyecto, String nombreProyecto, String descripcion, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion, boolean estado, UsuarioEntity usuario, ClientesEntity cliente, Disenios3dEntity disenio) {
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
        this.usuario = usuario;
        this.cliente = cliente;
        this.disenio = disenio;
    }

    public ProyectosEntity() {
    }

    // Getter & Setter


    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public ClientesEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClientesEntity cliente) {
        this.cliente = cliente;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Disenios3dEntity getDisenio() {
        return disenio;
    }

    public void setDisenio(Disenios3dEntity disenio) {
        this.disenio = disenio;
    }
}
