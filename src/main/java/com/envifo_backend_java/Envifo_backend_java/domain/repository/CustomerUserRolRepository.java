package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteUsuarioRolEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerUserRolRepository {
    void saveClienteUsuarioRolEntity (ClienteUsuarioRolEntity rolPorCliente);
    List<ClienteUsuarioRolEntity> getByClienteIdCliente(Long idCliente);
    Optional<ClienteUsuarioRolEntity> getByUsuarioIdUsuarioAndClienteIdCliente(Long idUsuario, Long idCliente);
    void deleteById (Long idAsignacion);
    Optional<ClienteUsuarioRolEntity> getByIdCliUsuRol (Long idCliUsuRol);
}
