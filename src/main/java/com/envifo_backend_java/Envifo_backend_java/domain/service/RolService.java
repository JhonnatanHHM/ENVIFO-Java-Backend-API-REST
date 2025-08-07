package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;

import java.util.Optional;

public interface RolService {

    Optional<RolDto> getByIdRol(Long idRol);
    Optional<RolDto> getByname(String name);
    RolDto editRol(RolDto rolDto);
    RolDto createRol(RolDto rolDto);
    void deleteByIdRol(Long idRol);
}
