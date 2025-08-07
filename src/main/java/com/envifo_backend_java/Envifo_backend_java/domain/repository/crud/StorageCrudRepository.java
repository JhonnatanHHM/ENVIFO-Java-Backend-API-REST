package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageCrudRepository extends JpaRepository<AlmacenamientoEntity, Long> {
    boolean existsByIdEntidadAndTipoEntidad(Long idEntidad, String tipoEntidad);
    Optional<AlmacenamientoEntity> findByIdEntidadAndTipoEntidad(Long idEntidad, String tipoEntidad);

}
