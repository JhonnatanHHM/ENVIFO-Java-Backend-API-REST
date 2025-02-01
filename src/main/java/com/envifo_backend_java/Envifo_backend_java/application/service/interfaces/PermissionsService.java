package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.domain.model.PermissionsDom;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;

import java.util.Optional;

public interface PermissionsService {
    Optional<PermissionsDom> getById(Long idPermiso);
}
