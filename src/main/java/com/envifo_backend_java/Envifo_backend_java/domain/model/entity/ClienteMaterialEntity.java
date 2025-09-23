package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes_materiales")
public class ClienteMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cli_mat", nullable = false)
    private Long idCliMat;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClientesEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    private MaterialesEntity material;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public ClienteMaterialEntity(Long idCliMat, ClientesEntity cliente, MaterialesEntity material) {
        this.idCliMat = idCliMat;
        this.cliente = cliente;
        this.material = material;
    }

    public ClienteMaterialEntity() {
    }

    public Long getIdCliMat() {
        return idCliMat;
    }

    public void setIdCliMat(Long idCliMat) {
        this.idCliMat = idCliMat;
    }

    public ClientesEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClientesEntity cliente) {
        this.cliente = cliente;
    }

    public MaterialesEntity getMaterial() {
        return material;
    }

    public void setMaterial(MaterialesEntity material) {
        this.material = material;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
