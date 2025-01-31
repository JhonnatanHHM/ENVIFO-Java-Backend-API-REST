package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;

import java.util.Optional;

public interface PermissionsRepository {
    Optional<PermisosEntity> getById(Long idPermiso);
    PermisosEntity save(PermisosEntity permisos);
    Optional<PermisosEntity> findByIdPermiso(Long idPermiso);
}
