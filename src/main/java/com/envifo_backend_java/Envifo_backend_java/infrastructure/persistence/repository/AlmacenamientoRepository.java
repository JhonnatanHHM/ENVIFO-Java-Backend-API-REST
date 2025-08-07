package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.StorageCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AlmacenamientoRepository implements StorageRepository {

    private StorageCrudRepository storageCrudRepository;

    @Autowired
    public AlmacenamientoRepository(StorageCrudRepository storageCrudRepository) {
        this.storageCrudRepository = storageCrudRepository;
    }

    @Override
    public Optional<AlmacenamientoEntity> findById(Long idArchivo) {
        return storageCrudRepository.findById(idArchivo);
    }

    @Override
    public AlmacenamientoEntity saveFile(AlmacenamientoEntity archivo) {
        return storageCrudRepository.save(archivo);
    }

    @Override
    public void deleteFileById(Long idArchivo) {
        storageCrudRepository.deleteById(idArchivo);
    }

    @Override
    public boolean existsByIdEntidadAndTipoEntidad(Long idEntidad, String tipoEntidad) {
        return storageCrudRepository.existsByIdEntidadAndTipoEntidad(idEntidad, tipoEntidad);
    }

    @Override
    public Optional<AlmacenamientoEntity> findByIdEntidadAndTipoEntidad(Long idEntidad, String tipoEntidad) {
        return storageCrudRepository.findByIdEntidadAndTipoEntidad(idEntidad,tipoEntidad);
    }
}
