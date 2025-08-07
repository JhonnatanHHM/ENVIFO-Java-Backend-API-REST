package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    boolean existsById (Long idCliente);

    Optional<ClientesEntity> getByEmail(String email);

    Boolean getExistsByEmail(String email);

    List<ClientesEntity> getAll();

    void deleteCustomer(Long idCliente);

    ClientesEntity saveCustomer(ClientesEntity cliente);

    Optional<ClientesEntity> getByIdCliente(Long idCliente);
}
