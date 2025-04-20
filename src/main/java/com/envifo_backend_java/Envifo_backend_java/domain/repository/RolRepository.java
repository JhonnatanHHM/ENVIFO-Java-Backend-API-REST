package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;

import java.util.Optional;

public interface RolRepository {

    Optional<RolesEntity> getByIdRol(Long idRol);
    Optional<RolesEntity> getByName(String name);
    RolesEntity save (RolesEntity rol);
    void deleteRol (Long idRol);

}
