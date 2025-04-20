package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.GradesDto;


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

    List<GradesDto> getGradesFilterByUser(String data, Long idUsuario);

    List<GradesDto> getGradesFilterByClient(String data, Long idCliente);
}
