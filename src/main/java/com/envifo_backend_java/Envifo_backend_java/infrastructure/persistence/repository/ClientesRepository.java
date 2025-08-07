package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CustomerRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientesRepository implements CustomerRepository {

    private final CustomerCrudRepository customerCrudRepository;

    @Autowired
    public ClientesRepository(CustomerCrudRepository customerCrudRepository) {
        this.customerCrudRepository = customerCrudRepository;
    }

    @Override
    public boolean existsById(Long idCliente) {
        return customerCrudRepository.existsById(idCliente);
    }

    @Override
    public Optional<ClientesEntity> getByEmail(String email) {
        return customerCrudRepository.findByEmail(email);
    }

    @Override
    public Boolean getExistsByEmail(String email) {
        return customerCrudRepository.existsByEmail(email);
    }

    @Override
    public List<ClientesEntity> getAll() {
        return customerCrudRepository.findAll();
    }

    @Override
    public void deleteCustomer(Long idCliente) {
        customerCrudRepository.deleteById(idCliente);
    }

    @Override
    public ClientesEntity saveCustomer(ClientesEntity cliente) {
        return customerCrudRepository.saveAndFlush(cliente);
    }

    @Override
    public Optional<ClientesEntity> getByIdCliente(Long idCliente) {
        return customerCrudRepository.findById(idCliente);
    }
}
