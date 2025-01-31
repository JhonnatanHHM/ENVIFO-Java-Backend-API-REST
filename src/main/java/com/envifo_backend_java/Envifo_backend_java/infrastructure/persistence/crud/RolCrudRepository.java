package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.crud;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolCrudRepository extends CrudRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(String name);
}
