package com.envifo_backend_java.Envifo_backend_java.application.dto;

/**
 * DTO para representar un diseño 3D, incluyendo su configuración, materiales y objetos.
 */
public class Designs3dDto {
    private Long idDisenio;
    private String configuracion;
    private String materiales;
    private String objetos;

    public Designs3dDto() {
    }

    public Designs3dDto(Long idDisenio, String configuracion, String materiales, String objetos) {
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

    public String getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }

    public String getMateriales() {
        return materiales;
    }

    public void setMateriales(String materiales) {
        this.materiales = materiales;
    }

    public String getObjetos() {
        return objetos;
    }

    public void setObjetos(String objetos) {
        this.objetos = objetos;
    }
}