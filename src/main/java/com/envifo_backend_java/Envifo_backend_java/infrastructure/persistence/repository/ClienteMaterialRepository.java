package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteMaterialEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerMaterialCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteMaterialRepository implements CustomerMaterialRepository {

    private CustomerMaterialCrudRepository customerMaterialCrudRepository;

    @Autowired
    public ClienteMaterialRepository(CustomerMaterialCrudRepository customerMaterialCrudRepository) {
        this.customerMaterialCrudRepository = customerMaterialCrudRepository;
    }

    @Override
    public ClienteMaterialEntity saveClienteMaterial(ClienteMaterialEntity clienteMaterial) {
        return customerMaterialCrudRepository.saveAndFlush(clienteMaterial);
    }

    @Override
    public List<MaterialesEntity> getMaterialByCliente(Long idCliente) {
        return customerMaterialCrudRepository.findMaterialesByClienteId(idCliente);
    }

    @Override
    public void deleteByMaterialId(Long idMaterial) {
        customerMaterialCrudRepository.deleteByMaterialId(idMaterial);
    }
}
