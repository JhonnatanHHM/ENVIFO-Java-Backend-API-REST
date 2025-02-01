package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;

import java.util.Optional;

public interface RolRepository {
    Optional<RolesEntity> getByName(String name);
    RolesEntity save (RolesEntity rol);
    // Verificar si existe un rol "RESTRINGIDO" con el idPermiso indicado
    boolean existsByPermisos_IdPermisoAndNameIgnoreCase(Long idPermiso, String name);
}
