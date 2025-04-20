package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;


import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserCrudRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

}
