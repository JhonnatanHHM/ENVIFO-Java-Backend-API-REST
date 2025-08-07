package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerCrudRepository extends JpaRepository<ClientesEntity, Long> {

    Optional<ClientesEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
