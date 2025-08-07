package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.application.dto.UserCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    boolean existsById (Long idUsuario);

    Optional<UsuarioEntity> getByEmail(String email);

    Boolean getExistsByEmail(String email);

    List<UsuarioEntity> getAll();

    void delete(Long idUsuario);

    UsuarioEntity save(UsuarioEntity usuario);

    Optional<UsuarioEntity> getByIdUsuario(Long idUsuario);

}
