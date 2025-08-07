package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaterialsCrudRepository extends JpaRepository<MaterialesEntity, Long> {

    List<MaterialesEntity> findAllByCategoriaNombreAndCategoriaClienteIdCliente(String nombreCategoria, Long idCliente);

    @Query("SELECT m FROM MaterialesEntity m JOIN m.clientes c WHERE c.idCliente = :idCliente")
    List<MaterialesEntity> findMaterialByClienteId(@Param("idCliente") Long idCliente);

    @Query("SELECT m FROM MaterialesEntity m WHERE m.clientes IS EMPTY")
    List<MaterialesEntity> findMaterialsGlobales();
}
