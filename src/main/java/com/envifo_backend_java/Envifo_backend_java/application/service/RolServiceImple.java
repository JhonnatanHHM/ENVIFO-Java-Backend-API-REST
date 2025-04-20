package com.envifo_backend_java.Envifo_backend_java.application.service;


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

    @Autowired
    private PermisosRepository permisosRepository;

    @Autowired
    private RolRepository roleRepository;

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
        rolDto.setPermisos(roles.getPermisos());

        return Optional.of(rolDto);
    }

    @Override
    public RolDto editRol(RolDto rolDto) {
        RolesEntity rolEntity = roleRepository.getByName(rolDto.getName())
                .orElseGet(() -> {
                    RolesEntity newRol = new RolesEntity();
                    newRol.setName(rolDto.getName());
                    newRol.setDescription(rolDto.getDescription());
                    newRol.setPermisos(rolDto.getPermisos());
                    return roleRepository.save(newRol); // Guardar el nuevo rol y retornarlo
                });

        // Convertir a DTO antes de devolverlo si es necesario
        return convertToDto(rolEntity);
    }

    @Override
    public RolDto createRol(RolDto rolDto) {
        RolesEntity rolEntity = roleRepository.getByName(rolDto.getName())
                .orElseGet(() -> {
                    RolesEntity newRol = new RolesEntity();
                    newRol.setName(rolDto.getName());
                    newRol.setDescription(rolDto.getDescription());

                    // Crear permisos predeterminados
                    PermisosEntity permisos = new PermisosEntity();
                    permisos.setEditPermisos(false);
                    permisos.setVistaUsuarios(true);
                    permisos.setEditUsuarios(false);
                    permisos.setVistaProyectos(true);
                    permisos.setEditProyectos(true);
                    permisos.setVistaDisenios3d(true);
                    permisos.setEditDisenios3d(true);
                    permisos.setVistaMateriales(true);
                    permisos.setEditMateriales(false);
                    permisos.setVistaInformes(false);
                    permisos.setVistaCategorias(true);
                    permisos.setEditCategorias(false);

                    // Guardar permisos en la base de datos
                    PermisosEntity savedPermisos = permisosRepository.save(permisos);

                    newRol.setPermisos(savedPermisos);

                    return roleRepository.save(newRol); // Guardar el nuevo rol y retornarlo
                });
        
        return convertToDto(rolEntity);
    }



    private RolDto convertToDto(RolesEntity rol) {
        RolDto dto = new RolDto();
        dto.setIdRol(rol.getIdRol());
        dto.setName(rol.getName());
        dto.setDescription(rol.getDescription());
        return dto;
    }


}