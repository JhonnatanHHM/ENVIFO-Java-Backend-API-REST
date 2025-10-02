package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "disenios_3d")
public class Disenios3dEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disenio", nullable = false)
    private Long idDisenio;

    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode configuracion;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode materiales;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode objetos;

    // Constructores
    public Disenios3dEntity(Long idDisenio, JsonNode configuracion, JsonNode materiales, JsonNode objetos) {
        this.idDisenio = idDisenio;
        this.configuracion = configuracion;
        this.materiales = materiales;
        this.objetos = objetos;
    }

    public Disenios3dEntity() {
    }

    // Getters y Setters
    public Long getIdDisenio() {
        return idDisenio;
    }

    public void setIdDisenio(Long idDisenio) {
        this.idDisenio = idDisenio;
    }

    public JsonNode getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(JsonNode configuracion) {
        this.configuracion = configuracion;
    }

    public JsonNode getMateriales() {
        return materiales;
    }

    public void setMateriales(JsonNode materiales) {
        this.materiales = materiales;
    }

    public JsonNode getObjetos() {
        return objetos;
    }

    public void setObjetos(JsonNode objetos) {
        this.objetos = objetos;
    }
}