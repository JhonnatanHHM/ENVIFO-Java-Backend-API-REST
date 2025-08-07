package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordTokenCrudRepository extends JpaRepository<PasswordTokenEntity,Long> {

    void deleteByExpirationBefore(LocalDateTime fecha);
    Optional<PasswordTokenEntity> findByToken(String token);
}
