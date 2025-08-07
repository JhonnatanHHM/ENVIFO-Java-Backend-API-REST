package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.TexturasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface TexturesCrudRepository extends JpaRepository<TexturasEntity, Long> {

    List<TexturasEntity> findAllByCategoriaNombreAndCategoriaSeccion(String nombreCategoria, String seccion);

}
