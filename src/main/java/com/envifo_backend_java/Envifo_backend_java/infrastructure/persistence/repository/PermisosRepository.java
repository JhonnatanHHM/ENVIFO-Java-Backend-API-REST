package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.PermissionsCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PermisosRepository implements PermissionsRepository {


    private PermissionsCrudRepository permissionsCrudRepository;

    @Autowired
    public PermisosRepository(PermissionsCrudRepository permissionsCrudRepository) {
        this.permissionsCrudRepository = permissionsCrudRepository;
    }

    @Override
    public PermisosEntity save(PermisosEntity permisos) {
        return permissionsCrudRepository.saveAndFlush(permisos);
    }

    public Optional<PermisosEntity> findByIdPermiso(Long idPermiso) {
        return  permissionsCrudRepository.findById(idPermiso);
    }

    @Override
    public void deleteByIdPermiso(Long idPermiso) {
        permissionsCrudRepository.deleteById(idPermiso);
    }
}
