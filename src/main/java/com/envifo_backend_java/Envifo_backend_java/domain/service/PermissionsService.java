package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;

import java.util.Optional;

public interface PermissionsService {
    Optional<PermissionsDto> getById(Long idPermiso);
}
