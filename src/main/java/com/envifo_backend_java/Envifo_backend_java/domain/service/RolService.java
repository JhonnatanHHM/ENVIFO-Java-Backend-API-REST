package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;

import java.util.Optional;

public interface RolService {

    Optional<RolDto> getByIdRol(Long idRol);
    public Optional<RolDto> getByname(String name);
    public RolDto editRol(RolDto rolDto);
    public RolDto createRol(RolDto rolDto);
}
