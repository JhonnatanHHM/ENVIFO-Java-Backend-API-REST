package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.domain.service.PermissionsService;
import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Optional;

public class PermissionsServiceImple implements PermissionsService {

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Override
    public Optional<PermissionsDto> getById(Long idPermiso) {

        PermisosEntity permisos = permissionsRepository.findByIdPermiso(idPermiso)
                .orElseThrow(() -> new NotFoundException("Permisos no encontrados!"));

        PermissionsDto permissionsDto = new PermissionsDto();
        permissionsDto.setIdPermiso(permisos.getIdPermiso());
        permissionsDto.setEditPermisos(permisos.isEditPermisos());
        permissionsDto.setVistaUsuarios(permissionsDto.isVistaUsuarios());
        permissionsDto.setEditUsuarios(permissionsDto.isEditUsuarios());
        permissionsDto.setVistaProyectos(permissionsDto.isVistaProyectos());
        permissionsDto.setEditProyectos(permisos.isEditProyectos());
        permissionsDto.setVistaDisenios3d(permissionsDto.isVistaDisenios3d());
        permissionsDto.setEditDisenios3d(permisos.isEditDisenios3d());
        permissionsDto.setVistaMateriales(permissionsDto.isVistaMateriales());
        permissionsDto.setEditMateriales(permissionsDto.isEditMateriales());
        permissionsDto.setVistaInformes(permisos.isVistaInformes());
        permissionsDto.setVistaCategorias(permisos.isVistaCategorias());
        permissionsDto.setEditCategorias(permissionsDto.isEditCategorias());

        return Optional.of(permissionsDto);
    }
}
