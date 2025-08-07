package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "objetos")
public class ObjetosEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objetos", nullable = false)
    private Long idObjeto;

    @Column(name = "nombre_objeto",nullable = false, length = 100)
    private String nombreObjeto;

    @Column(nullable = false)
    private BigDecimal alto;

    @Column(nullable = false)
    private BigDecimal ancho;

    @Column(nullable = false)
    private BigDecimal profundidad;

    @Column(nullable = false)
    private boolean estado;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriasEntity categoria;

    // Constructores

    public ObjetosEntity(Long idObjeto, String nombreObjeto, BigDecimal alto, BigDecimal ancho, BigDecimal profundidad, boolean estado, String metadata, CategoriasEntity categoria) {
        this.idObjeto = idObjeto;
        this.nombreObjeto = nombreObjeto;
        this.alto = alto;
        this.ancho = ancho;
        this.profundidad = profundidad;
        this.estado = estado;
        this.metadata = metadata;
        this.categoria = categoria;
    }

    public ObjetosEntity() {
    }

    // Getter & Setter

    public Long getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Long idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public BigDecimal getAlto() {
        return alto;
    }

    public void setAlto(BigDecimal alto) {
        this.alto = alto;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public BigDecimal getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(BigDecimal profundidad) {
        this.profundidad = profundidad;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public CategoriasEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriasEntity categoria) {
        this.categoria = categoria;
    }
}
