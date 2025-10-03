package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.Designs3dDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.Designs3dServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.Designs3dRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Designs3dServiceImplTest {

    @Mock
    private Designs3dRepository repository;

    private ObjectMapper objectMapper;

    @InjectMocks
    private Designs3dServiceImpl service;

    private Designs3dDto dto;
    private Disenios3dEntity entity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        // Manually inject ObjectMapper into service
        service = new Designs3dServiceImpl(repository, objectMapper);

        // Configurar DTO
        dto = new Designs3dDto();
        dto.setIdDisenio(1L);
        dto.setConfiguracion("{\"luces\":{\"ambiental\":0.8,\"direccional\":{\"intensidad\":1.5,\"color\":\"#ffffff\"}},\"objetos\":[]}");
        dto.setMateriales("[789]");
        dto.setObjetos("[]");

        // Configurar Entidad
        entity = new Disenios3dEntity();
        entity.setIdDisenio(1L);
        entity.setConfiguracion(objectMapper.readTree(dto.getConfiguracion()));
        entity.setMateriales(objectMapper.readTree(dto.getMateriales()));
        entity.setObjetos(objectMapper.readTree(dto.getObjetos()));
    }

    @Test
    void testSaveDesign() {
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.saveDesign(dto);

        assertNotNull(result, "Result should not be null");
        assertEquals(entity.getIdDisenio(), result.getIdDisenio(), "ID should match");
        assertEquals(entity.getConfiguracion().toString(), result.getConfiguracion().toString(), "Configuracion should match");
        assertEquals(entity.getMateriales().toString(), result.getMateriales().toString(), "Materiales should match");
        assertEquals(entity.getObjetos().toString(), result.getObjetos().toString(), "Objetos should match");
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testSaveDesignWithNullFields() {
        Designs3dDto nullDto = new Designs3dDto();
        nullDto.setIdDisenio(1L);
        // configuracion, materiales, and objetos are null

        Disenios3dEntity nullEntity = new Disenios3dEntity();
        nullEntity.setIdDisenio(1L);
        nullEntity.setConfiguracion(null);
        nullEntity.setMateriales(objectMapper.createArrayNode());
        nullEntity.setObjetos(objectMapper.createArrayNode());

        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(nullEntity);

        Disenios3dEntity result = service.saveDesign(nullDto);

        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getIdDisenio(), "ID should match");
        assertNull(result.getConfiguracion(), "Configuracion should be null");
        assertTrue(result.getMateriales().isArray(), "Materiales should be an array");
        assertEquals(0, result.getMateriales().size(), "Materiales should be empty");
        assertTrue(result.getObjetos().isArray(), "Objetos should be an array");
        assertEquals(0, result.getObjetos().size(), "Objetos should be empty");
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testUpdateDesign() {
        when(repository.getDesignById(1L)).thenReturn(Optional.of(entity));
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.updateDesign(dto);

        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getIdDisenio(), "ID should match");
        assertEquals(entity.getConfiguracion().toString(), result.getConfiguracion().toString(), "Configuracion should match");
        assertEquals(entity.getMateriales().toString(), result.getMateriales().toString(), "Materiales should match");
        assertEquals(entity.getObjetos().toString(), result.getObjetos().toString(), "Objetos should match");
        verify(repository, times(1)).getDesignById(1L);
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testUpdateDesignWithNullFields() {
        Designs3dDto nullDto = new Designs3dDto();
        nullDto.setIdDisenio(1L);
        // configuracion, materiales, and objetos are null

        when(repository.getDesignById(1L)).thenReturn(Optional.of(entity));
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.updateDesign(nullDto);

        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getIdDisenio(), "ID should match");
        assertEquals(entity.getConfiguracion().toString(), result.getConfiguracion().toString(), "Configuracion should remain unchanged");
        assertEquals(entity.getMateriales().toString(), result.getMateriales().toString(), "Materiales should remain unchanged");
        assertEquals(entity.getObjetos().toString(), result.getObjetos().toString(), "Objetos should remain unchanged");
        verify(repository, times(1)).getDesignById(1L);
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testUpdateDesignNotFound() {
        when(repository.getDesignById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.updateDesign(dto);
        });

        assertEquals("Diseño no encontrado", exception.getMessage());
        verify(repository, times(1)).getDesignById(1L);
        verify(repository, never()).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testDeleteDesign() {
        doNothing().when(repository).deleteDesign(1L);

        service.deleteDesign(1L);

        verify(repository, times(1)).deleteDesign(1L);
    }
}