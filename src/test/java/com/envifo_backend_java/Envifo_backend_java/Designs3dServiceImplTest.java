package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.Designs3dDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.Designs3dServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.Designs3dRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Designs3dServiceImplTest {

    @Mock
    private Designs3dRepository repository;

    @InjectMocks
    private Designs3dServiceImpl service;

    private Designs3dDto dto;
    private Disenios3dEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new Designs3dDto(1L, "config1", "material1", "objeto1");

        entity = new Disenios3dEntity();
        entity.setIdDisenio(1L);
        entity.setConfiguracion("config1");
        entity.setMateriales("material1");
        entity.setObjetos("objeto1");
    }

    @Test
    void testSaveDesign() {
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.saveDesign(dto);

        assertNotNull(result);
        assertEquals("config1", result.getConfiguracion());
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testUpdateDesign() {
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.updateDesign(dto);

        assertNotNull(result);
        assertEquals(1L, result.getIdDisenio());
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testGetDesignById_Found() {
        when(repository.getDesignById(1L)).thenReturn(Optional.of(entity));

        Optional<Designs3dDto> result = service.getDesignById(1L);

        assertTrue(result.isPresent());
        assertEquals("material1", result.get().getMateriales());
        verify(repository, times(1)).getDesignById(1L);
    }

    @Test
    void testGetDesignById_NotFound() {
        when(repository.getDesignById(2L)).thenReturn(Optional.empty());

        Optional<Designs3dDto> result = service.getDesignById(2L);

        assertFalse(result.isPresent());
        verify(repository, times(1)).getDesignById(2L);
    }

    @Test
    void testDeleteDesign() {
        doNothing().when(repository).deleteDesign(1L);

        service.deleteDesign(1L);

        verify(repository, times(1)).deleteDesign(1L);
    }
}
