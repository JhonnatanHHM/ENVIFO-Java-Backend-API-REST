package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.domain.model.GradesDto;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.NotasEntity;

import java.util.List;
import java.util.Optional;

public interface GradeService {

    List<GradesDto> searchByTitleOrContent(String data);

    List<GradesDto> getAllGrades();

    void delete(Long idNota);

    void editGrade(GradesDto gradesDto);

    GradesDto save(GradesDto grade);

    Optional<GradesDto> getByIdGrade(Long idGrade);

    List<GradesDto> getByIdUsuario(Long idUsuario);
}
