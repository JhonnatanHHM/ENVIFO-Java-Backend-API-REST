package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.Designs3dDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.Designs3dServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.Designs3dRepository;
import com.fasterxml.jackson.databind.JsonNode;
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

    @InjectMocks
    private Designs3dServiceImpl service;

    private Designs3dDto dto;
    private Disenios3dEntity entity;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();

        // Crear JsonNode para los campos
        JsonNode configuracionNode;
        JsonNode materialesNode;
        JsonNode objetosNode;
        try {
            configuracionNode = objectMapper.readTree("{\"luces\":{\"ambiental\":0.8,\"direccional\":{\"intensidad\":1.5,\"color\":\"#ffffff\"}},\"objetos\":[]}");
            materialesNode = objectMapper.readTree("[789]");
            objetosNode = objectMapper.readTree("[]");
        } catch (Exception e) {
            throw new RuntimeException("Error al crear JsonNode para el test", e);
        }

        // Configurar DTO
        dto = new Designs3dDto();
        dto.setIdDisenio(1L);
        dto.setConfiguracion(configuracionNode);
        dto.setMateriales(materialesNode);
        dto.setObjetos(objetosNode);

        // Configurar Entidad
        entity = new Disenios3dEntity();
        entity.setIdDisenio(1L);
        entity.setConfiguracion(configuracionNode);
        entity.setMateriales(materialesNode);
        entity.setObjetos(objetosNode);
    }

    @Test
    void testSaveDesign() {
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.saveDesign(dto);

        assertNotNull(result);
        assertEquals(entity.getConfiguracion(), result.getConfiguracion());
        assertEquals(entity.getMateriales(), result.getMateriales());
        assertEquals(entity.getObjetos(), result.getObjetos());
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testUpdateDesign() {
        when(repository.saveDesign(any(Disenios3dEntity.class))).thenReturn(entity);

        Disenios3dEntity result = service.updateDesign(dto);

        assertNotNull(result);
        assertEquals(1L, result.getIdDisenio());
        assertEquals(entity.getConfiguracion(), result.getConfiguracion());
        assertEquals(entity.getMateriales(), result.getMateriales());
        assertEquals(entity.getObjetos(), result.getObjetos());
        verify(repository, times(1)).saveDesign(any(Disenios3dEntity.class));
    }

    @Test
    void testGetDesignById_Found() {
        when(repository.getDesignById(1L)).thenReturn(Optional.of(entity));

        Optional<Designs3dDto> result = service.getDesignById(1L);

        assertTrue(result.isPresent());
        assertEquals(dto.getConfiguracion(), result.get().getConfiguracion());
        assertEquals(dto.getMateriales(), result.get().getMateriales());
        assertEquals(dto.getObjetos(), result.get().getObjetos());
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