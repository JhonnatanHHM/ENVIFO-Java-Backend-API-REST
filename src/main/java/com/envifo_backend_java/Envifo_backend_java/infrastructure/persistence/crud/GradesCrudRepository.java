package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.crud;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.NotasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GradesCrudRepository extends JpaRepository<NotasEntity, Long> {

    @Query("SELECT n FROM NotasEntity n WHERE LOWER(n.titulo) LIKE LOWER(CONCAT('%', :data, '%')) " +
            "OR LOWER(n.contenido) LIKE LOWER(CONCAT('%', :data, '%'))")
    List<NotasEntity> buscarPorTituloOContenido(@Param("data") String data);

    List<NotasEntity> findByIdUsuario_IdUsuario(Long idUsuario);
}
