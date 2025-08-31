package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes_materiales")
public class ClienteMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cli_mat", nullable = false)
    private Long idCliMat;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClientesEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    private MaterialesEntity material;

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
}
