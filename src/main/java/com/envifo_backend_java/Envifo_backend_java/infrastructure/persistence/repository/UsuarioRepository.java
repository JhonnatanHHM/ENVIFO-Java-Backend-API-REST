package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.UserRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.crud.UserCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository implements UserRepository {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Override
    public Optional<UsuarioEntity> getByEmail(String email) {
        return userCrudRepository.findByEmail(email);
    }

    @Override
    public Boolean getExistsByEmail(String email) {
        return userCrudRepository.existsByEmail(email);
    }

    @Override
    public List<UsuarioEntity> getAll() {
        return userCrudRepository.findAll();
    }


    @Override
    public void delete(Long idUsuario) {
        userCrudRepository.deleteById(idUsuario);
    }

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        return userCrudRepository.save(usuario);
    }

    @Override
    public Optional<UsuarioEntity> getByIdUsuario(Long idUsuario) {
        return userCrudRepository.findById(idUsuario);
    }

}
