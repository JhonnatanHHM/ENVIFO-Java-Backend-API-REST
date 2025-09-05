package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.MaterialsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerMaterialCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.MaterialsCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MaterialesRepository implements MaterialsRepository {

    private MaterialsCrudRepository materialsCrudRepository;

    private CustomerMaterialCrudRepository customerMaterialCrudRepository;

    @Autowired
    public MaterialesRepository(MaterialsCrudRepository materialsCrudRepository, CustomerMaterialCrudRepository customerMaterialCrudRepository) {
        this.materialsCrudRepository = materialsCrudRepository;
        this.customerMaterialCrudRepository = customerMaterialCrudRepository;
    }

    @Override
    public Optional<MaterialesEntity> getByIdMaterial(Long idMaterial) {
        return materialsCrudRepository.findById(idMaterial);
    }

    @Override
    public List<MaterialesEntity> getMaterialByNameAndSectionCategory(String nameCategory, Long idCliente) {
        return materialsCrudRepository.findAllByCategoriaNombreAndCategoriaClienteIdCliente(nameCategory, idCliente);
    }

    @Override
    public List<MaterialesEntity> getObjectsByIdsMaterials(List<Long> idsMaterials) {
        return materialsCrudRepository.findAllById(idsMaterials);
    }

    @Override
    public List<MaterialesEntity> findMaterialsGlobales() {
        return materialsCrudRepository.findMaterialsGlobales();
    }

    @Override
    public MaterialesEntity getLastMaterialByCustomer(Long idCustomer) {
        return customerMaterialCrudRepository.findUltimoMaterialByCliente(idCustomer);
    }

    @Override
    public MaterialesEntity saveMaterial(MaterialesEntity material) {
        return materialsCrudRepository.saveAndFlush(material);
    }

    @Override
    public void deleteMaterial(Long idMaterial) {
        materialsCrudRepository.deleteById(idMaterial);
    }
}
