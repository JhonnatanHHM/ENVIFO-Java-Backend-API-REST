package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;

import java.util.Optional;

public interface Designs3dRepository {

    Optional<Disenios3dEntity> getDesignById (Long idDesign);

    Disenios3dEntity saveDesign (Disenios3dEntity design);

    void deleteDesign(Long idDesign);
}
