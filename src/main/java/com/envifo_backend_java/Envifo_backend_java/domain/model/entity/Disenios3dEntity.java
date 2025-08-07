package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "disenios_3d")
public class Disenios3dEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disenio", nullable = false)
    private Long idDisenio;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String configuracion;

    @Column(columnDefinition = "jsonb")
    private String materiales;

    @Column(columnDefinition = "jsonb")
    private String objetos;

    // Constructores


    public Disenios3dEntity(Long idDisenio, String configuracion, String materiales, String objetos) {
        this.idDisenio = idDisenio;
        this.configuracion = configuracion;
        this.materiales = materiales;
        this.objetos = objetos;
    }

    public Disenios3dEntity() {
    }

    // Getter & Setter


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
