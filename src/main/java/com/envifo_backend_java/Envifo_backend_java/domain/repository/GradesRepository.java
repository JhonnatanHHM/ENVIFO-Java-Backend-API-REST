package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.NotasEntity;

import java.util.List;
import java.util.Optional;

public interface GradesRepository {

    List<NotasEntity> searchByTitleOrContent(String data);

    List<NotasEntity> getAllGrades();

    void delete(Long idNota);

    NotasEntity save(NotasEntity nota);

    Optional<NotasEntity> getByIdGrade(Long idNota);

    List<NotasEntity> getByIdUsuario(Long idUsuario);

    List<NotasEntity> getGradesFilterByUser(String data, Long idUsuario);

    List<NotasEntity> getGradesFilterByClient(String data, Long idCliente);

}
