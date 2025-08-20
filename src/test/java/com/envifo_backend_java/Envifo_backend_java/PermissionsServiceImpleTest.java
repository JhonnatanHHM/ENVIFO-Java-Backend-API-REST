package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.PermissionsServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.PermissionsRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionsServiceImpleTest {

    @InjectMocks
    private PermissionsServiceImple service;

    @Mock
    private PermissionsRepository permissionsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- savePermission ---
    @Test
    void testSavePermission_success() {
        PermissionsDto dto = new PermissionsDto();
        dto.setEditPermisos(true);
        dto.setVistaUsuarios(true);
        dto.setEditUsuarios(true);
        dto.setVistaProyectos(true);
        dto.setEditProyectos(true);
        dto.setEditDisenios3d(true);
        dto.setVistaMateriales(true);
        dto.setEditMateriales(true);
        dto.setEditCategorias(true);

        PermisosEntity saved = new PermisosEntity();
        saved.setIdPermiso(1L);

        when(permissionsRepository.save(any(PermisosEntity.class))).thenReturn(saved);

        PermisosEntity result = service.savePermission(dto);

        assertNotNull(result);
        assertEquals(1L, result.getIdPermiso());
        verify(permissionsRepository, times(1)).save(any(PermisosEntity.class));
    }

    // --- editPermissions ---
    @Test
    void testEditPermissions_success() {
        PermissionsDto dto = new PermissionsDto();
        dto.setIdPermiso(10L);
        dto.setEditPermisos(true);
        dto.setVistaUsuarios(true);
        dto.setEditUsuarios(true);
        dto.setVistaProyectos(true);
        dto.setEditProyectos(true);
        dto.setEditDisenios3d(true);
        dto.setVistaMateriales(true);
        dto.setEditMateriales(true);
        dto.setEditCategorias(true);

        PermisosEntity updated = new PermisosEntity();
        updated.setIdPermiso(10L);

        when(permissionsRepository.save(any(PermisosEntity.class))).thenReturn(updated);

        PermisosEntity result = service.editPermissions(dto);

        assertNotNull(result);
        assertEquals(10L, result.getIdPermiso());
        verify(permissionsRepository, times(1)).save(any(PermisosEntity.class));
    }

    @Test
    void testEditPermissions_restrictedIds_throwException() {
        PermissionsDto dto = new PermissionsDto();
        dto.setIdPermiso(3L);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                service.editPermissions(dto)
        );

        assertTrue(ex.getMessage().contains("No se pueden realizar cambios"));
        verify(permissionsRepository, never()).save(any());
    }

    // --- getByIdPermiso ---
    @Test
    void testGetByIdPermiso_found() {
        PermisosEntity permisos = new PermisosEntity();
        permisos.setIdPermiso(5L);
        permisos.setEditPermisos(true);
        permisos.setEditUsuarios(true);
        permisos.setVistaUsuarios(true);
        permisos.setEditProyectos(true);
        permisos.setVistaProyectos(true);
        permisos.setEditDisenios3d(true);
        permisos.setVistaDisenios3d(true);
        permisos.setEditMateriales(true);
        permisos.setVistaMateriales(true);
        permisos.setEditCategorias(true);
        permisos.setVistaCategorias(true);
        permisos.setVistaInformes(true);

        when(permissionsRepository.findByIdPermiso(5L)).thenReturn(Optional.of(permisos));

        Optional<PermissionsDto> result = service.getByIdPermiso(5L);

        assertTrue(result.isPresent());
        assertEquals(5L, result.get().getIdPermiso());
        verify(permissionsRepository, times(1)).findByIdPermiso(5L);
    }

    @Test
    void testGetByIdPermiso_notFound() {
        when(permissionsRepository.findByIdPermiso(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                service.getByIdPermiso(99L)
        );

        verify(permissionsRepository, times(1)).findByIdPermiso(99L);
    }

    // --- deleteByIdPermiso ---
    @Test
    void testDeleteByIdPermiso_success() {
        service.deleteByIdPermiso(7L);
        verify(permissionsRepository, times(1)).deleteByIdPermiso(7L);
    }
}
