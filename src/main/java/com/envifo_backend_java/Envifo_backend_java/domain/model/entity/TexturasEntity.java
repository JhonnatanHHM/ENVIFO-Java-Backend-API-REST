package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "texturas")
public class TexturasEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_textura")
    private Long idTextura;

    @Column(name = "nombre_textura", nullable = false, length = 100)
    private String nombreTextura;

    @Column(length = 300)
    private String descripcion;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriasEntity categoria;

    // Constructor


    public TexturasEntity(Long idTextura, String nombreTextura, String descripcion, CategoriasEntity categoria) {
        this.idTextura = idTextura;
        this.nombreTextura = nombreTextura;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public TexturasEntity() {
    }

    // Getter & Setter

    public Long getIdTextura() {
        return idTextura;
    }

    public void setIdTextura(Long idTextura) {
        this.idTextura = idTextura;
    }

    public String getNombreTextura() {
        return nombreTextura;
    }

    public void setNombreTextura(String nombreTextura) {
        this.nombreTextura = nombreTextura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriasEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriasEntity categoria) {
        this.categoria = categoria;
    }
}
