package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.service.PermissionsServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.RolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.RolRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolServiceImpleTest {

    @Mock
    private PermissionsServiceImple permissionsServiceImple;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImple rolService;

    private RolesEntity rolEntity;
    private RolDto rolDto;
    private PermisosEntity permisos;
    private PermissionsDto permisosDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        permisos = new PermisosEntity();
        permisos.setIdPermiso(10L);
        permisos.setEditUsuarios(true);

        rolEntity = new RolesEntity();
        rolEntity.setIdRol(4L);
        rolEntity.setName("ADMIN");
        rolEntity.setDescription("Rol administrador");
        rolEntity.setPermisos(permisos);

        permisosDto = new PermissionsDto();
        permisosDto.setIdPermiso(10L);
        permisosDto.setEditUsuarios(true);

        rolDto = new RolDto();
        rolDto.setIdRol(1L);
        rolDto.setName("ADMIN");
        rolDto.setDescription("Rol administrador");
        rolDto.setPermisos(permisosDto);
    }

    @Test
    void testGetByIdRol_found() {
        when(rolRepository.getByIdRol(1L)).thenReturn(Optional.of(rolEntity));

        Optional<RolDto> result = rolService.getByIdRol(1L);

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
    }

    @Test
    void testGetByIdRol_notFound() {
        when(rolRepository.getByIdRol(99L)).thenReturn(Optional.empty());

        Optional<RolDto> result = rolService.getByIdRol(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetByName_found() {
        when(rolRepository.getByName("ADMIN")).thenReturn(Optional.of(rolEntity));

        Optional<RolDto> result = rolService.getByname("ADMIN");

        assertTrue(result.isPresent());
        assertEquals("Rol administrador", result.get().getDescription());
    }

    @Test
    void testGetByName_notFound() {
        when(rolRepository.getByName("USER")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> rolService.getByname("USER"));
    }

    @Test
    void testEditRol_reservedNameGlobal() {
        rolDto.setName("GLOBAL");

        assertThrows(RuntimeException.class, () -> rolService.editRol(rolDto));
    }

    @Test
    void testEditRol_reservedNameRestringido() {
        rolDto.setName("RESTRINGIDO");

        assertThrows(RuntimeException.class, () -> rolService.editRol(rolDto));
    }

    @Test
    void testEditRol_createsNewRoleIfNotExists() {
        rolDto.setIdRol(99L); // usar un id que NO sea 1 ni 2
        rolDto.getPermisos().setIdPermiso(99L); // idem, evitar 1 o 2

        when(rolRepository.getByIdRol(99L)).thenReturn(Optional.empty());
        when(rolRepository.save(any())).thenReturn(rolEntity);

        RolDto result = rolService.editRol(rolDto);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(permissionsServiceImple).editPermissions(any());
        verify(rolRepository).save(any());
    }


    @Test
    void testCreateRol_reservedNameRestringido() {
        rolDto.setName("RESTRINGIDO");

        assertThrows(RuntimeException.class, () -> rolService.createRol(rolDto));
    }

    @Test
    void testCreateRol_reservedNameGlobal() {
        rolDto.setName("GLOBAL");

        assertThrows(RuntimeException.class, () -> rolService.createRol(rolDto));
    }

    @Test
    void testCreateRol_duplicateName() {
        when(rolRepository.getByName("ADMIN")).thenReturn(Optional.of(rolEntity));

        assertThrows(RuntimeException.class, () -> rolService.createRol(rolDto));
    }

    @Test
    void testCreateRol_success() {
        when(rolRepository.getByName("ADMIN")).thenReturn(Optional.empty());
        when(permissionsServiceImple.savePermission(any())).thenReturn(permisos);
        when(rolRepository.save(any())).thenReturn(rolEntity);

        RolDto result = rolService.createRol(rolDto);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(permissionsServiceImple).savePermission(any());
        verify(rolRepository).save(any());
    }

    @Test
    void testDeleteByIdRol() {
        rolService.deleteByIdRol(1L);

        verify(rolRepository).deleteRol(1L);
    }
}
