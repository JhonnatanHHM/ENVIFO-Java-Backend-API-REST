package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.PermissionsService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.PermissionsDom;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Optional;

public class PermissionsServiceImple implements PermissionsService {

    @Autowired
    private PermissionsRepository permissionsRepository;

    @Override
    public Optional<PermissionsDom> getById(Long idPermiso) {

        PermisosEntity permisos = permissionsRepository.findByIdPermiso(idPermiso)
                .orElseThrow(() -> new NotFoundException("Permisos no encontrados!"));

        PermissionsDom permissionsDom = new PermissionsDom();
        permissionsDom.setIdPermiso(permisos.getIdPermiso());
        permissionsDom.setEditPermisos(permisos.isEditPermisos());
        permissionsDom.setVistaUsuarios(permissionsDom.isVistaUsuarios());
        permissionsDom.setEditUsuarios(permissionsDom.isEditUsuarios());
        permissionsDom.setVistaProyectos(permissionsDom.isVistaProyectos());
        permissionsDom.setEditProyectos(permisos.isEditProyectos());
        permissionsDom.setVistaDisenios3d(permissionsDom.isVistaDisenios3d());
        permissionsDom.setEditDisenios3d(permisos.isEditDisenios3d());
        permissionsDom.setVistaMateriales(permissionsDom.isVistaMateriales());
        permissionsDom.setEditMateriales(permissionsDom.isEditMateriales());
        permissionsDom.setVistaInformes(permisos.isVistaInformes());
        permissionsDom.setVistaCategorias(permisos.isVistaCategorias());
        permissionsDom.setEditCategorias(permissionsDom.isEditCategorias());

        return Optional.of(permissionsDom);
    }
}
