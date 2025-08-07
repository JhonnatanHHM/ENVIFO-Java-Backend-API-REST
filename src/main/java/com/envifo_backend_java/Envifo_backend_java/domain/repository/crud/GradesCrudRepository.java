package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.NotasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GradesCrudRepository extends JpaRepository<NotasEntity, Long> {

    @Query("SELECT n FROM NotasEntity n WHERE LOWER(n.titulo) LIKE LOWER(CONCAT('%', :data, '%')) " +
            "OR LOWER(n.contenido) LIKE LOWER(CONCAT('%', :data, '%'))")
    List<NotasEntity> buscarPorTituloOContenido(@Param("data") String data);

    @Query("SELECT n FROM NotasEntity n WHERE (LOWER(n.titulo) LIKE LOWER(CONCAT('%', :data, '%')) " +
            "OR LOWER(n.contenido) LIKE LOWER(CONCAT('%', :data, '%'))) " +
            "AND n.usuario.idUsuario = :idUsuario")
    List<NotasEntity> buscarNotasFiltradasPorUsuario(@Param("data") String data, @Param("idUsuario") Long idUsuario);

    @Query("SELECT n FROM NotasEntity n WHERE (LOWER(n.titulo) LIKE LOWER(CONCAT('%', :data, '%')) " +
            "OR LOWER(n.contenido) LIKE LOWER(CONCAT('%', :data, '%'))) " +
            "AND n.cliente.idCliente = :idCliente")
    List<NotasEntity> buscarNotasFiltradasPorCliente(@Param("data") String data, @Param("idCliente") Long idCliente);

    List<NotasEntity> findByUsuario_IdUsuario(Long idUsuario);

    List<NotasEntity> findByClienteIdCliente(Long idCliente);
}
