package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.crud.PermissionsCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PermisosRepository implements PermissionsRepository {

    @Autowired
    private PermissionsCrudRepository permissionsCrudRepository;

    @Override
    public Optional<PermisosEntity> getById(Long idPermiso) {
        return permissionsCrudRepository.findById(idPermiso);
    }

    @Override
    public PermisosEntity save(PermisosEntity permisos) {
        return permissionsCrudRepository.save(permisos);
    }

    public Optional<PermisosEntity> findByIdPermiso(Long idPermiso) {
        return  permissionsCrudRepository.findById(idPermiso);
    }
}
