package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PasswordTokenEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PasswordTokenRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.PasswordTokenCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class ContraseñaTokenRepository implements PasswordTokenRepository {

    @Autowired
    private PasswordTokenCrudRepository passwordTokenCrudRepository;

    @Override
    public void deleteByExpirationBefore(LocalDateTime fecha) {
        passwordTokenCrudRepository.deleteByExpirationBefore(fecha);
    }

    @Override
    public Optional<PasswordTokenEntity> GetByToken(String token){
        return passwordTokenCrudRepository.findByToken(token);
    }

    @Override
    public PasswordTokenEntity save(PasswordTokenEntity passwordToken) {
        return passwordTokenCrudRepository.save(passwordToken);
    }

}
