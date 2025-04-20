package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PasswordTokenEntity;

import java.util.Optional;

public interface PasswordTokenRepository {

    Optional<PasswordTokenEntity> GetByToken(String token);

    PasswordTokenEntity save(PasswordTokenEntity passwordToken);
}
