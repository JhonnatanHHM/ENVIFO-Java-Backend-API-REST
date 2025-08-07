package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ProyectosEntity;

import java.util.List;
import java.util.Optional;

public interface ProjectsRepository {

    Optional<ProyectosEntity> getByIdProject(Long idProjecto);
    List<ProyectosEntity> getProjectByCustomer(Long idCliente);
    List<ProyectosEntity> getProjectByUser(Long idUsuario);
    List<ProyectosEntity> getProjectByUserAndCustomer(Long idUsuario, Long idCliente);
    ProyectosEntity saveProject (ProyectosEntity projecto);
    void deleteProject (Long idProjecto);
}

