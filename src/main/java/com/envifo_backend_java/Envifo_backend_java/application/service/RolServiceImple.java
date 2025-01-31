package com.envifo_backend_java.Envifo_backend_java.application.service;


import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.RolService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.RolDom;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.RolRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolServiceImple implements RolService {

    @Autowired
    private RolRepository roleRepository;

    @Override
    public Optional<RolDom> getByname(String name) {

        RolesEntity roles = roleRepository.getByName(name)
                .orElseThrow(() -> new NotFoundException("Roles no encontrados!"));

        RolDom rolDom = new RolDom();
        rolDom.setIdRol(roles.getIdRol());
        rolDom.setName(roles.getName());
        rolDom.setDescription(roles.getDescription());
        rolDom.setPermisos(roles.getPermisos());

        return Optional.of(rolDom);
    }
}