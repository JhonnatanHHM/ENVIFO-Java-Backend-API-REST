package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteMaterialEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;

import java.util.List;

public interface CustomerMaterialRepository {

    ClienteMaterialEntity saveClienteMaterial (ClienteMaterialEntity clienteMaterial);
    List<MaterialesEntity> getMaterialByCliente (Long idCliente);
    void deleteByMaterialId (Long idMaterial);
}
