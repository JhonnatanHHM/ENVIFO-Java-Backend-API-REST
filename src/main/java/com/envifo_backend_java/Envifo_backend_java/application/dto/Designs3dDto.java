package com.envifo_backend_java.Envifo_backend_java.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class Designs3dDto {
    private Long idDisenio;
    private JsonNode configuracion;
    private JsonNode materiales;
    private JsonNode objetos;

    public Designs3dDto() {
    }

    public Designs3dDto(Long idDisenio, JsonNode configuracion, JsonNode materiales, JsonNode objetos) {
        this.idDisenio = idDisenio;
        this.configuracion = configuracion;
        this.materiales = materiales;
        this.objetos = objetos;
    }

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