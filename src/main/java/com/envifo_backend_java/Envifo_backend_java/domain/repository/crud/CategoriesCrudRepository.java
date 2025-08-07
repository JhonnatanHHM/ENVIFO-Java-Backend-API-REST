package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoriesCrudRepository extends JpaRepository<CategoriasEntity, Long> {

    List<CategoriasEntity> findAllByClienteIdCliente(Long idCliente);

    List<CategoriasEntity> findAllByNombreIn(List<String> nombres);

}
