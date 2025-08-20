package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materiales")
public class MaterialesEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material", nullable = false)
    private Long idMaterial;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion_mate",length = 300)
    private String descripcionMate;

    @Column(nullable = false)
    private BigDecimal alto;

    @Column(nullable = false)
    private BigDecimal ancho;

    @Column(nullable = false)
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriasEntity categoria;

    @ManyToOne
    @JoinColumn(name = "id_textura")
    private TexturasEntity textura;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "clientes_materiales",
            joinColumns = @JoinColumn(name = "id_material"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente")
    )
    private List<ClientesEntity> clientes = new ArrayList<>();

    // Constructores


    public MaterialesEntity(Long idMaterial, String nombre, String descripcionMate, BigDecimal alto, BigDecimal ancho, boolean estado, CategoriasEntity categoria, TexturasEntity textura, List<ClientesEntity> clientes) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
        this.descripcionMate = descripcionMate;
        this.alto = alto;
        this.ancho = ancho;
        this.estado = estado;
        this.categoria = categoria;
        this.textura = textura;
        this.clientes = clientes;
    }

    public MaterialesEntity() {
    }

    // Getter & Setter


    public Long getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(Long idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcionMate() {
        return descripcionMate;
    }

    public void setDescripcionMate(String descripcionMate) {
        this.descripcionMate = descripcionMate;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public CategoriasEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriasEntity categoria) {
        this.categoria = categoria;
    }

    public TexturasEntity getTextura() {
        return textura;
    }

    public void setTextura(TexturasEntity textura) {
        this.textura = textura;
    }

    public List<ClientesEntity> getClientes() {
        return clientes;
    }

    public void setClientes(List<ClientesEntity> clientes) {
        this.clientes = clientes;
    }
}
