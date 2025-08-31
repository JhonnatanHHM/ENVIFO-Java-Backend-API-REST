package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialsCrudRepository extends JpaRepository<MaterialesEntity, Long> {

    List<MaterialesEntity> findAllByCategoriaNombreAndCategoriaClienteIdCliente(String nombreCategoria, Long idCliente);

    @Query("SELECT m FROM MaterialesEntity m WHERE m.clienteMateriales IS EMPTY")
    List<MaterialesEntity> findMaterialsGlobales();

}
