package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.GradesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.crud.GradesCrudRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.NotasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotasRepository implements GradesRepository {

    @Autowired
    GradesCrudRepository gradesCrudRepository;

    @Override
    public List<NotasEntity> searchByTitleOrContent(String data) {
        return gradesCrudRepository.buscarPorTituloOContenido(data);
    }

    @Override
    public List<NotasEntity> getAllGrades() {
        return gradesCrudRepository.findAll();
    }

    @Override
    public void delete(Long idNota) {
        gradesCrudRepository.deleteById(idNota);
    }

    @Override
    public NotasEntity save(NotasEntity nota) {
        return gradesCrudRepository.save(nota);
    }

    @Override
    public Optional<NotasEntity> getByIdGrade(Long idNota) {
        return gradesCrudRepository.findById(idNota);
    }

    @Override
    public List<NotasEntity> getByIdUsuario(Long idUsuario) {
        return gradesCrudRepository.findByIdUsuario_IdUsuario(idUsuario);
    }

}
