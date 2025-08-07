package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "escenarios")
public class EscenariosEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_escenario")
    private Long idEscenario;

    @Column(name = "nombre_escenario", nullable = false, length = 100)
    private String nombreEscenario;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false)
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriasEntity categoria;

    @Column(columnDefinition = "jsonb")
    private String metadata;

    // Constructores

    public EscenariosEntity(Long idEscenario, String nombreEscenario, String descripcion, boolean estado, CategoriasEntity categoria, String metadata) {
        this.idEscenario = idEscenario;
        this.nombreEscenario = nombreEscenario;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.metadata = metadata;
    }

    public EscenariosEntity() {
    }

    // Getter & Setter


    public Long getIdEscenario() {
        return idEscenario;
    }

    public void setIdEscenario(Long idEscenario) {
        this.idEscenario = idEscenario;
    }

    public String getNombreEscenario() {
        return nombreEscenario;
    }

    public void setNombreEscenario(String nombreEscenario) {
        this.nombreEscenario = nombreEscenario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
