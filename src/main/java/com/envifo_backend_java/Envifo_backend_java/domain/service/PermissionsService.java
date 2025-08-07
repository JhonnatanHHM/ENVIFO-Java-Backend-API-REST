package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;

import java.util.Optional;

public interface PermissionsService {
    PermisosEntity savePermission(PermissionsDto permisos);
    PermisosEntity editPermissions (PermissionsDto permisosEditados);
    Optional<PermissionsDto> getByIdPermiso(Long idPermiso);
    void deleteByIdPermiso (Long idPermiso);
}
