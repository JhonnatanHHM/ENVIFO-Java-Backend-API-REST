package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionsServiceImpleTest {

    @Mock
    private PermissionsRepository permissionsRepository;

    @InjectMocks
    private PermissionsServiceImple permissionsServiceImple;

    private PermissionsDto dto;
    private PermisosEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new PermissionsDto();
        dto.setIdPermiso(10L);
        dto.setEditUsuarios(true);
        dto.setVistaUsuarios(false);

        entity = new PermisosEntity();
        entity.setIdPermiso(10L);
        entity.setEditUsuarios(false);
        entity.setVistaUsuarios(true);
    }

    @Test
    void testSavePermission_success() {
        when(permissionsRepository.save(any(PermisosEntity.class))).thenReturn(entity);

        PermisosEntity result = permissionsServiceImple.savePermission(dto);

        assertNotNull(result);
        assertEquals(10L, result.getIdPermiso());
        verify(permissionsRepository, times(1)).save(any(PermisosEntity.class));
    }

    @Test
    void testEditPermissions_success() {
        when(permissionsRepository.findByIdPermiso(10L)).thenReturn(Optional.of(entity));
        when(permissionsRepository.save(any(PermisosEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PermisosEntity result = permissionsServiceImple.editPermissions(dto);

        assertNotNull(result);
        assertEquals(10L, result.getIdPermiso());
        assertTrue(result.isEditUsuarios());
        assertFalse(result.isVistaUsuarios());
        verify(permissionsRepository, times(1)).save(any(PermisosEntity.class));
    }

    @Test
    void testEditPermissions_restrictedIds_throwException() {
        dto.setIdPermiso(1L);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            permissionsServiceImple.editPermissions(dto);
        });

        assertTrue(exception.getMessage().contains("No se pueden realizar cambios"));
        verify(permissionsRepository, never()).save(any(PermisosEntity.class));
    }

    @Test
    void testEditPermissions_notFound_throwException() {
        dto.setIdPermiso(99L);
        when(permissionsRepository.findByIdPermiso(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            permissionsServiceImple.editPermissions(dto);
        });

        assertTrue(exception.getMessage().contains("Permisos no encontrados con ID: 99"));
    }

    @Test
    void testGetByIdPermiso_success() {
        when(permissionsRepository.findByIdPermiso(10L)).thenReturn(Optional.of(entity));

        Optional<PermissionsDto> result = permissionsServiceImple.getByIdPermiso(10L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getIdPermiso());
        assertTrue(result.get().isVistaUsuarios());
    }

    @Test
    void testGetByIdPermiso_notFound_throwException() {
        when(permissionsRepository.findByIdPermiso(20L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> permissionsServiceImple.getByIdPermiso(20L));
    }

    @Test
    void testDeleteByIdPermiso_success() {
        doNothing().when(permissionsRepository).deleteByIdPermiso(10L);

        permissionsServiceImple.deleteByIdPermiso(10L);

        verify(permissionsRepository, times(1)).deleteByIdPermiso(10L);
    }
}
