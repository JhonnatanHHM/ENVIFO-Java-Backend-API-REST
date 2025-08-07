package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ProyectosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ProjectsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.ProjectsCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProyectosRepository implements ProjectsRepository {


    private ProjectsCrudRepository projectsCrudRepository;

    @Autowired
    public ProyectosRepository(ProjectsCrudRepository projectsCrudRepository) {
        this.projectsCrudRepository = projectsCrudRepository;
    }

    @Override
    public Optional<ProyectosEntity> getByIdProject(Long idProjecto) {
        return projectsCrudRepository.findById(idProjecto);
    }

    @Override
    public List<ProyectosEntity> getProjectByCustomer(Long idCliente) {
        return projectsCrudRepository.findAllByClienteIdCliente(idCliente);
    }

    @Override
    public List<ProyectosEntity> getProjectByUser(Long idUsuario) {
        return projectsCrudRepository.findAllByUsuarioIdUsuario(idUsuario);
    }

    @Override
    public List<ProyectosEntity> getProjectByUserAndCustomer(Long idUsuario, Long idCliente) {
        return projectsCrudRepository.findAllByUsuarioIdUsuarioAndClienteIdCliente(idUsuario, idCliente);
    }

    @Override
    public ProyectosEntity saveProject(ProyectosEntity projecto) {
        return projectsCrudRepository.saveAndFlush(projecto);
    }

    @Override
    public void deleteProject(Long idProjecto) {
        projectsCrudRepository.deleteById(idProjecto);
    }
}
