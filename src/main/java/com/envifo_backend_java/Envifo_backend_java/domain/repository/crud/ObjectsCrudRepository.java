package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ObjetosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectsCrudRepository extends JpaRepository<ObjetosEntity, Long> {

    List<ObjetosEntity> findAllByCategoriaNombreAndCategoriaSeccion(String nombreCategoria, String seccion);
}
