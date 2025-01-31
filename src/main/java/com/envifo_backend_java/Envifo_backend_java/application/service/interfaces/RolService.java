package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;

import java.util.Optional;

public interface RolService {
    public Optional<RolesEntity> getByname(String name);
}
