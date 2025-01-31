package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.PermissionsService;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PermissionsServiceImple implements PermissionsService {

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Override
    public Optional<PermisosEntity> getById(Long idPermiso) {
        return permissionsRepository.getById(idPermiso);
    }
}
