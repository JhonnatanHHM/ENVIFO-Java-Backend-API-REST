package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;

import java.util.List;
import java.util.Optional;

public interface MaterialsRepository {

    Optional<MaterialesEntity> getByIdMaterial(Long idMaterial);
    List<MaterialesEntity> getMaterialByNameAndSectionCategory(String nameCategory, Long idCliente);
    List<MaterialesEntity> getObjectsByIdsMaterials(List<Long> idsMaterials);
    List<MaterialesEntity> getMaterialByClienteId(Long idCliente);
    List<MaterialesEntity> findMaterialsGlobales();
    MaterialesEntity saveMaterial (MaterialesEntity material);
    void deleteMaterial (Long idMaterial);
}
