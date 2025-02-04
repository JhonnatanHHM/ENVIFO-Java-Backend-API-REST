package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.domain.model.PermissionsDto;

import java.util.Optional;

public interface PermissionsService {
    Optional<PermissionsDto> getById(Long idPermiso);
}
