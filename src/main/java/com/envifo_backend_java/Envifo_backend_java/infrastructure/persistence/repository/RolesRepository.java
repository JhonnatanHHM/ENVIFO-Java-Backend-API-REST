package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.RolRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.RolCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RolesRepository implements RolRepository {

    private RolCrudRepository rolCrudRepository;

    @Autowired
    public RolesRepository(RolCrudRepository rolCrudRepository) {
        this.rolCrudRepository = rolCrudRepository;
    }

    @Override
    public Optional<RolesEntity> getByIdRol(Long idRol) {
        return rolCrudRepository.findById(idRol);
    }

    @Override
    public Optional<RolesEntity> getByName(String name) {
        return rolCrudRepository.findByName(name);
    }

    @Override
    public RolesEntity save(RolesEntity rol) {
        return rolCrudRepository.saveAndFlush(rol);
    }

    @Override
    public void deleteRol(Long idRol) {
        rolCrudRepository.deleteById(idRol);
    }

}
