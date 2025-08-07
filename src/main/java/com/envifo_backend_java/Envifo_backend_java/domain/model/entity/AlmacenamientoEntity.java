package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "almacenamiento")
public class AlmacenamientoEntity {

    // Atributos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_archivo", nullable = false)
    private Long idArchivo;

    @Column(name = "nombre_archivo", nullable = false)
    private String nombreArchivo;

    @Column(name = "llave_r2", nullable = false)
    private String llaveR2;

    @Column(name = "tipo_entidad", nullable = false)
    private String tipoEntidad;

    @Column(name = "id_entidad", nullable = false)
    private Long idEntidad;

    // Constructor

    public AlmacenamientoEntity(Long idArchivo, String nombreArchivo, String llaveR2, String tipoEntidad, Long idEntidad) {
        this.idArchivo = idArchivo;
        this.nombreArchivo = nombreArchivo;
        this.llaveR2 = llaveR2;
        this.tipoEntidad = tipoEntidad;
        this.idEntidad = idEntidad;
    }

    public AlmacenamientoEntity() {
    }

    // Getter & Setter


    public Long getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(Long idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getLlaveR2() {
        return llaveR2;
    }

    public void setLlaveR2(String llaveR2) {
        this.llaveR2 = llaveR2;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public Long getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Long idEntidad) {
        this.idEntidad = idEntidad;
    }
}
