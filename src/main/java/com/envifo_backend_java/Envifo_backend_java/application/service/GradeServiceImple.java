package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.GradeService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.GradesDto;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.NotasEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.NotasRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeServiceImple implements GradeService {

    @Autowired
    NotasRepository notasRepository;

    @Override
    public List<GradesDto> searchByTitleOrContent(String data) {
        List<NotasEntity> notas = notasRepository.searchByTitleOrContent(data);
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<GradesDto> getAllGrades() {
        List<NotasEntity> notas = notasRepository.getAllGrades();
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long idNota) {
        notasRepository.delete(idNota);
    }

    @Override
    public void editGrade(GradesDto gradesDto) {
        NotasEntity nota = convertToEntity(gradesDto);
        notasRepository.save(nota);
    }

    @Override
    public GradesDto save(GradesDto gradeDto) {
        NotasEntity nota = convertToEntity(gradeDto);
        NotasEntity savedNota = notasRepository.save(nota);
        return convertToDto(savedNota);
    }

    @Override
    public Optional<GradesDto> getByIdGrade(Long idGrade) {
        return notasRepository.getByIdGrade(idGrade).map(this::convertToDto);
    }

    @Override
    public List<GradesDto> getByIdUsuario(Long idUsuario) {
        List<NotasEntity> notas = notasRepository.getByIdUsuario(idUsuario);
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private GradesDto convertToDto(NotasEntity nota) {
        GradesDto gradesDto = new GradesDto();
        gradesDto.setIdGrade(nota.getIdNota());
        gradesDto.setTitle(nota.getTitulo());
        gradesDto.setContent(nota.getContenido());
        gradesDto.setIdUser(nota.getIdUsuario());
        return gradesDto;
    }

    private NotasEntity convertToEntity(GradesDto dto) {
        NotasEntity nota = new NotasEntity();
        nota.setIdNota(dto.getIdGrade());
        nota.setTitulo(dto.getTitle());
        nota.setContenido(dto.getContent());
        return nota;
    }
}
