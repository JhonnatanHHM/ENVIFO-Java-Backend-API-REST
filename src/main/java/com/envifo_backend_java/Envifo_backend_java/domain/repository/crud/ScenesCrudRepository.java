package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.EscenariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenesCrudRepository extends JpaRepository<EscenariosEntity, Long> {

    List<EscenariosEntity> findAllByCategoriaNombreAndCategoriaSeccion(String nombreCategoria, String seccion);
}
