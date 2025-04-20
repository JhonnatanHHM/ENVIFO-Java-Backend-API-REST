package com.envifo_backend_java.Envifo_backend_java.domain.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente_usuario_rol", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_usuario", "id_cliente"})
})
public class ClienteUsuarioRolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cli_usu_rol")
    private Long idCliUsuRol;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private ClientesEntity cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rol")
    private RolesEntity rol;

    // Getters, Setters


    public Long getIdCliUsuRol() {
        return idCliUsuRol;
    }

    public void setIdCliUsuRol(Long idCliUsuRol) {
        this.idCliUsuRol = idCliUsuRol;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public ClientesEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClientesEntity cliente) {
        this.cliente = cliente;
    }

    public RolesEntity getRol() {
        return rol;
    }

    public void setRol(RolesEntity rol) {
        this.rol = rol;
    }
}
