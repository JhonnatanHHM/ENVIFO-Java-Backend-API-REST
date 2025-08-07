package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;

import java.util.Optional;

public interface StorageRepository {

    Optional<AlmacenamientoEntity> findById (Long idArchivo);
    AlmacenamientoEntity saveFile (AlmacenamientoEntity archivo);
    void deleteFileById (Long idArchivo);
    boolean existsByIdEntidadAndTipoEntidad(Long idEntidad, String tipoEntidad);
    Optional<AlmacenamientoEntity> findByIdEntidadAndTipoEntidad(Long idEntidad, String tipoEntidad);

}
