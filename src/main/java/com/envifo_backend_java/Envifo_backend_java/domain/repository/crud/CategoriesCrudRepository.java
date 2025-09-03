package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.MaterialesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CategoriesCrudRepository extends JpaRepository<CategoriasEntity, Long> {

    List<CategoriasEntity> findAllByClienteIdCliente(Long idCliente);

    List<CategoriasEntity> findAllBySeccionIn(String seccion);

    @Query("SELECT c FROM CategoriasEntity c WHERE c.id_cliente IS EMPTY")
    List<CategoriasEntity> findCategoriesGlobales();

}
