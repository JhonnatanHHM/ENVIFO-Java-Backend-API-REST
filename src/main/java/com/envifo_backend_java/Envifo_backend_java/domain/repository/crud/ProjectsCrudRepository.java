package com.envifo_backend_java.Envifo_backend_java.domain.repository.crud;


import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ProyectosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectsCrudRepository extends JpaRepository<ProyectosEntity, Long> {

    List<ProyectosEntity> findAllByClienteIdCliente(Long idCliente);

    List<ProyectosEntity> findAllByUsuarioIdUsuario(Long idUsuario);

    List<ProyectosEntity> findAllByUsuarioIdUsuarioAndClienteIdCliente(Long idUsuario, Long idCliente);
}
