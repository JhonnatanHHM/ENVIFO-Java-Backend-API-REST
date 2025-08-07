package com.envifo_backend_java.Envifo_backend_java.application.service;


import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.RolService;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.RolRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.PermisosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class RolServiceImple implements RolService {


    private PermissionsServiceImple permissionsServiceImple;

    private RolRepository roleRepository;

    @Autowired
    public RolServiceImple(PermissionsServiceImple permissionsServiceImple, RolRepository roleRepository) {
        this.permissionsServiceImple = permissionsServiceImple;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<RolDto> getByIdRol(Long idRol) {
        return roleRepository.getByIdRol(idRol).map(this::convertToDto);
    }

    @Override
    public Optional<RolDto> getByname(String name) {

        RolesEntity roles = roleRepository.getByName(name)
                .orElseThrow(() -> new NotFoundException("Roles no encontrados!"));

        RolDto rolDto = new RolDto();
        rolDto.setIdRol(roles.getIdRol());
        rolDto.setName(roles.getName());
        rolDto.setDescription(roles.getDescription());
        rolDto.setPermisos(convertToPermissionsDto(roles.getPermisos()));

        return Optional.of(rolDto);
    }

    @Override
    public RolDto editRol(RolDto rolDto) {

        if ((rolDto.getName()).equals("GLOBAL") || (rolDto.getName()).equals("RESTRINGIDO")) {
            throw new RuntimeException("No se pueden realizar cambios a los roles GLOBALES o RESTRINGIDOS predeterminados");
        }

        RolesEntity rolEntity = roleRepository.getByName(rolDto.getName())
                .orElseGet(() -> {
                    RolesEntity newRol = new RolesEntity();
                    newRol.setIdRol(rolDto.getIdRol());
                    newRol.setName(rolDto.getName());
                    newRol.setDescription(rolDto.getDescription());

                    permissionsServiceImple.editPermissions(rolDto.getPermisos());

                    newRol.setPermisos(convertToPermisosEntity(rolDto.getPermisos()));
                    return roleRepository.save(newRol); // Guardar el nuevo rol y retornarlo
                });

        // Convertir a DTO antes de devolverlo si es necesario
        return convertToDto(rolEntity);
    }

    @Override
    public RolDto createRol(RolDto rolDto) {
        // Restricción de nombres no modificables
        if ("RESTRINGIDO".equalsIgnoreCase(rolDto.getName())) {
            throw new RuntimeException("El rol 'RESTRINGIDO' no es modificable");
        }

        if ("GLOBAL".equalsIgnoreCase(rolDto.getName())) {
            throw new RuntimeException("El rol 'GLOBAL' no es modificable");
        }

        // Validación: evitar duplicidad por nombre
        if (roleRepository.getByName(rolDto.getName()).isPresent()) {
            throw new RuntimeException("Ya existe un rol con el nombre '" + rolDto.getName() + "'");
        }

        // Crear nuevo rol
        RolesEntity newRol = new RolesEntity();
        newRol.setName(rolDto.getName());
        newRol.setDescription(rolDto.getDescription());

        // Guardar permisos y asignarlos
        PermisosEntity permisos = permissionsServiceImple.savePermission(rolDto.getPermisos());
        newRol.setPermisos(permisos);

        // Guardar rol
        RolesEntity savedRol = roleRepository.save(newRol);

        return convertToDto(savedRol);
    }


    @Override
    public void deleteByIdRol(Long idRol) {
        roleRepository.deleteRol(idRol);
    }


    private RolDto convertToDto(RolesEntity rol) {
        RolDto dto = new RolDto();
        dto.setIdRol(rol.getIdRol());
        dto.setName(rol.getName());
        dto.setDescription(rol.getDescription());
        dto.setPermisos(convertToPermissionsDto(rol.getPermisos()));
        return dto;
    }

    private PermissionsDto convertToPermissionsDto (PermisosEntity permisos) {
        PermissionsDto dto = new PermissionsDto();
        dto.setIdPermiso(permisos.getIdPermiso());
        dto.setEditPermisos(permisos.isEditPermisos());
        dto.setVistaUsuarios(permisos.isVistaUsuarios());
        dto.setEditUsuarios(permisos.isEditUsuarios());
        dto.setVistaProyectos(permisos.isVistaProyectos());
        dto.setEditProyectos(permisos.isEditProyectos());
        dto.setVistaDisenios3d(permisos.isEditPermisos());
        dto.setEditDisenios3d(permisos.isEditDisenios3d());
        dto.setVistaMateriales(permisos.isVistaMateriales());
        dto.setEditMateriales(permisos.isEditMateriales());
        dto.setVistaInformes(permisos.isEditPermisos());
        dto.setVistaCategorias(permisos.isEditPermisos());
        dto.setEditCategorias(permisos.isEditCategorias());

        return dto;
    }

    private PermisosEntity convertToPermisosEntity(PermissionsDto dto) {
        PermisosEntity permisos = new PermisosEntity();
        permisos.setIdPermiso(dto.getIdPermiso());
        permisos.setEditPermisos(dto.isEditPermisos());
        permisos.setVistaUsuarios(dto.isVistaUsuarios());
        permisos.setEditUsuarios(dto.isEditUsuarios());
        permisos.setVistaProyectos(dto.isVistaProyectos());
        permisos.setEditProyectos(dto.isEditProyectos());
        permisos.setVistaDisenios3d(dto.isVistaDisenios3d());
        permisos.setEditDisenios3d(dto.isEditDisenios3d());
        permisos.setVistaMateriales(dto.isVistaMateriales());
        permisos.setEditMateriales(dto.isEditMateriales());
        permisos.setVistaInformes(dto.isVistaInformes());
        permisos.setVistaCategorias(dto.isVistaCategorias());
        permisos.setEditCategorias(dto.isEditCategorias());

        return permisos;
    }

}