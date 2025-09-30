package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.domain.service.PermissionsService;
import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class PermissionsServiceImple implements PermissionsService {


    private PermissionsRepository permissionsRepository;

    @Autowired
    public PermissionsServiceImple(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public PermisosEntity savePermission(PermissionsDto permissionsDto) {
        // Crear permisos predeterminados
        PermisosEntity permisos = new PermisosEntity();
        permisos.setEditPermisos(permissionsDto.isEditPermisos());
        permisos.setVistaUsuarios(permissionsDto.isVistaUsuarios());
        permisos.setEditUsuarios(permissionsDto.isEditUsuarios());
        permisos.setVistaProyectos(permissionsDto.isVistaProyectos());
        permisos.setEditProyectos(permissionsDto.isEditProyectos());
        permisos.setVistaDisenios3d(permissionsDto.isVistaDisenios3d());
        permisos.setEditDisenios3d(permissionsDto.isEditDisenios3d());
        permisos.setVistaMateriales(permissionsDto.isVistaMateriales());
        permisos.setEditMateriales(permissionsDto.isEditMateriales());
        permisos.setVistaInformes(permissionsDto.isVistaInformes());
        permisos.setVistaCategorias(permissionsDto.isVistaCategorias());
        permisos.setEditCategorias(permissionsDto.isEditCategorias());

        // Guardar permisos en la base de datos
        return permissionsRepository.save(permisos);
    }

    @Override
    public PermisosEntity editPermissions(PermissionsDto permisosEditados) {

        if (permisosEditados.getIdPermiso() == 3 || permisosEditados.getIdPermiso() == 16) {
            throw new RuntimeException("No se pueden realizar cambios a los permisos GLOBALES o RESTRINGIDOS predeterminados");
        }

        // editar permisos predeterminados
        PermisosEntity permisos = new PermisosEntity();
        permisos.setIdPermiso(permisosEditados.getIdPermiso());
        permisos.setEditPermisos(permisosEditados.isEditPermisos());
        permisos.setVistaUsuarios(permisosEditados.isVistaUsuarios());
        permisos.setEditUsuarios(permisosEditados.isEditUsuarios());
        permisos.setVistaProyectos(permisosEditados.isVistaProyectos());
        permisos.setEditProyectos(permisosEditados.isEditProyectos());
        permisos.setVistaDisenios3d(permisosEditados.isVistaDisenios3d());
        permisos.setEditDisenios3d(permisosEditados.isEditDisenios3d());
        permisos.setVistaMateriales(permisosEditados.isVistaMateriales());
        permisos.setEditMateriales(permisosEditados.isEditMateriales());
        permisos.setVistaInformes(permisosEditados.isVistaInformes());
        permisos.setVistaCategorias(permisosEditados.isVistaCategorias());
        permisos.setEditCategorias(permisosEditados.isEditCategorias());

        // Guardar permisos en la base de datos
        return permissionsRepository.save(permisos);
    }

    @Override
    public Optional<PermissionsDto> getByIdPermiso(Long idPermiso) {

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

    @Override
    public void deleteByIdPermiso(Long idPermiso) {
        permissionsRepository.deleteByIdPermiso(idPermiso);
    }
}
