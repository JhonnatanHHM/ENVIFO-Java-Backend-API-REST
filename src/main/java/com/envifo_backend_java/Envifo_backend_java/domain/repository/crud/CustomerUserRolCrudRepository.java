package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteUsuarioRolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerUserRolCrudRepository extends JpaRepository<ClienteUsuarioRolEntity, Long> {
    List<ClienteUsuarioRolEntity> findByClienteIdCliente(Long idCliente);
    Optional<ClienteUsuarioRolEntity> findByUsuarioIdUsuarioAndClienteIdCliente(Long idUsuario, Long idCliente);
}