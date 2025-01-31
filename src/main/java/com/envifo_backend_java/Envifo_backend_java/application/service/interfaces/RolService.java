package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.domain.model.RolDom;

import java.util.Optional;

public interface RolService {
    public Optional<RolDom> getByname(String name);
}
