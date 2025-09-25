package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.service.GradeService;
import com.envifo_backend_java.Envifo_backend_java.application.dto.GradesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.NotasEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.NotasRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeServiceImple implements GradeService {

    @Autowired
    public NotasRepository notasRepository;

    @Override
    public List<GradesDto> searchByTitleOrContent(String data) {
        List<NotasEntity> notas = notasRepository.searchByTitleOrContent(data);
        return notas.stream().map(this::convertToDto).toList();
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

    @Override
    public List<GradesDto> getByIdCustomer(Long idCliente) {
        List<NotasEntity> notas = notasRepository.getByIdCliente(idCliente);
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<GradesDto> getGradesFilterByUser(String data, Long idUsuario) {
        List<NotasEntity> notas = notasRepository.getGradesFilterByUser(data, idUsuario);
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<GradesDto> getGradesFilterByClient(String data, Long idCliente) {
        List<NotasEntity> notas = notasRepository.getGradesFilterByClient(data, idCliente);
        return notas.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private GradesDto convertToDto(NotasEntity nota) {
        GradesDto gradesDto = new GradesDto();
        gradesDto.setIdGrade(nota.getIdNota());
        gradesDto.setTitle(nota.getTitulo());
        gradesDto.setContent(nota.getContenido());

        // Validar si existe usuario
        if (nota.getUsuario() != null) {
            gradesDto.setIdUser(nota.getUsuario().getIdUsuario());
        }

        // Validar si existe cliente
        if (nota.getCliente() != null) {
            gradesDto.setIdCustomer(nota.getCliente().getIdCliente());
        }

        return gradesDto;
    }

    private NotasEntity convertToEntity(GradesDto dto) {
        NotasEntity nota = new NotasEntity();
        nota.setIdNota(dto.getIdGrade());
        nota.setTitulo(dto.getTitle());
        nota.setContenido(dto.getContent());

        // Solo asignar si viene un idUsuario válido
        if (dto.getIdUser() != null) {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setIdUsuario(dto.getIdUser());
            nota.setUsuario(usuario);
        }

        // Solo asignar si viene un idCliente válido
        if (dto.getIdCustomer() != null) {
            ClientesEntity cliente = new ClientesEntity();
            cliente.setIdCliente(dto.getIdCustomer());
            nota.setCliente(cliente);
        }

        return nota;
    }

}
