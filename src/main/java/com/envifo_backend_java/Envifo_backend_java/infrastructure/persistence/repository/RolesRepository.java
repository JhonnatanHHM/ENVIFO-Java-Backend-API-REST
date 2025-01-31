package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.RolRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.crud.RolCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RolesRepository implements RolRepository {

    @Autowired
    private RolCrudRepository rolCrudRepository;

    @Override
    public Optional<RolesEntity> getByName(String name) {
        return rolCrudRepository.findByName(name);
    }

    @Override
    public RolesEntity save(RolesEntity rol) {
        return rolCrudRepository.save(rol);
    }
}
