package com.envifo_backend_java.Envifo_backend_java.domain.repository;


import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.UsuarioEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<UsuarioEntity> getByEmail(String email);

    Boolean getExistsByEmail(String email);

    List<UsuarioEntity> getAll();

    void delete(Long idUsuario);

    UsuarioEntity save(UsuarioEntity usuario);

    Optional<UsuarioEntity> getByIdUsuario(Long idUsuario);

}
