package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteUsuarioRolEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CustomerUserRolRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerUserRolCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteUsuarioRolRepository implements CustomerUserRolRepository {

    private CustomerUserRolCrudRepository customerUserRolCrudRepository;

    @Autowired
    public ClienteUsuarioRolRepository(CustomerUserRolCrudRepository customerUserRolCrudRepository) {
        this.customerUserRolCrudRepository = customerUserRolCrudRepository;
    }

    @Override
    public void saveClienteUsuarioRolEntity(ClienteUsuarioRolEntity rolPorCliente) {
        customerUserRolCrudRepository.save(rolPorCliente);
    }


    @Override
    public List<ClienteUsuarioRolEntity> getByClienteIdCliente(Long idCliente) {
        return customerUserRolCrudRepository.findByClienteIdCliente(idCliente);
    }

    @Override
    public Optional<ClienteUsuarioRolEntity> getByUsuarioIdUsuarioAndClienteIdCliente(Long idUsuario, Long idCliente) {
        return customerUserRolCrudRepository.findByUsuarioIdUsuarioAndClienteIdCliente(idUsuario,idCliente);
    }

    @Override
    public void deleteById(Long idAsignacion) {
        customerUserRolCrudRepository.deleteById(idAsignacion);
    }

    @Override
    public Optional<ClienteUsuarioRolEntity> getByIdCliUsuRol(Long idCliUsuRol) {
        return customerUserRolCrudRepository.findById(idCliUsuRol);
    }
}
