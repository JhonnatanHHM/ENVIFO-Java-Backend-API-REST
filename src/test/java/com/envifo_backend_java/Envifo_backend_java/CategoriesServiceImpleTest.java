package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CategoriesDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.CategoriesServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriesServiceImpleTest {

    @Mock
    private CategoriesRepository categoriesRepository;

    @Mock
    private ClientesRepository clientesRepository;

    @InjectMocks
    private CategoriesServiceImple categoriesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCategory() {
        // Arrange
        CategoriesDto dto = new CategoriesDto();
        dto.setNombre("Madera");
        dto.setSection("Interiores");
        dto.setIdCliente(1L);

        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(1L);

        CategoriasEntity savedEntity = new CategoriasEntity();
        savedEntity.setIdCategoria(10L);
        savedEntity.setNombre("Madera");
        savedEntity.setSeccion("Interiores");
        savedEntity.setEstado(true);
        savedEntity.setCliente(cliente);

        when(clientesRepository.getByIdCliente(1L)).thenReturn(Optional.of(cliente));
        when(categoriesRepository.saveCategory(any(CategoriasEntity.class))).thenReturn(savedEntity);

        // Act
        CategoriesDto result = categoriesService.saveCategory(dto);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getIdCategoria());
        assertEquals("Madera", result.getNombre());
        assertEquals(1L, result.getIdCliente());
        verify(clientesRepository, times(1)).getByIdCliente(1L);
        verify(categoriesRepository, times(1)).saveCategory(any(CategoriasEntity.class));
    }

    @Test
    void testUpdateCategory() {
        // Arrange
        CategoriesDto dto = new CategoriesDto();
        dto.setIdCategoria(5L);
        dto.setNombre("Mármol");
        dto.setSection("Exteriores");
        dto.setEstado(true);
        dto.setIdCliente(2L);

        CategoriasEntity existente = new CategoriasEntity();
        existente.setIdCategoria(5L);
        existente.setNombre("Viejo");
        existente.setSeccion("X");
        existente.setEstado(false);
        existente.setCliente(new ClientesEntity());

        when(categoriesRepository.getCategoryById(5L)).thenReturn(Optional.of(existente));
        when(categoriesRepository.saveCategory(any(CategoriasEntity.class))).thenReturn(existente);

        // Act
        CategoriesDto result = categoriesService.updateCategory(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Mármol", result.getNombre());
        assertEquals("Exteriores", result.getSection());
        assertEquals(2L, result.getIdCliente());
        verify(categoriesRepository, times(1)).getCategoryById(5L);
        verify(categoriesRepository, times(1)).saveCategory(any(CategoriasEntity.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        // Arrange
        CategoriesDto dto = new CategoriesDto();
        dto.setIdCategoria(99L);

        when(categoriesRepository.getCategoryById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> categoriesService.updateCategory(dto));
        verify(categoriesRepository, times(1)).getCategoryById(99L);
    }

    @Test
    void testGetCategoriesBySection() {
        // Arrange
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(7L);

        CategoriasEntity entity = new CategoriasEntity();
        entity.setIdCategoria(50L);
        entity.setNombre("Vidrio");
        entity.setSeccion("Interiores");
        entity.setEstado(true);
        entity.setCliente(cliente);

        when(categoriesRepository.getAllBySeccionIn("Interiores")).thenReturn(List.of(entity));

        // Act
        List<CategoriesDto> result = categoriesService.getCategoriesBySection("Interiores");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Vidrio", result.get(0).getNombre());
        assertEquals("Interiores", result.get(0).getSection());
        verify(categoriesRepository, times(1)).getAllBySeccionIn("Interiores");
    }

    @Test
    void testGetCategoriesGlobals() {
        // Arrange
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(8L);

        CategoriasEntity entity = new CategoriasEntity();
        entity.setIdCategoria(60L);
        entity.setNombre("Global");
        entity.setSeccion("Todos");
        entity.setEstado(true);
        entity.setCliente(cliente);

        when(categoriesRepository.getCategoriesGlobals()).thenReturn(List.of(entity));

        // Act
        List<CategoriesDto> result = categoriesService.getCategoriesGlobals();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Global", result.get(0).getNombre());
        assertEquals("Todos", result.get(0).getSection());
        verify(categoriesRepository, times(1)).getCategoriesGlobals();
    }


    @Test
    void testGetCategoryById() {
        // Arrange
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(3L);

        CategoriasEntity entity = new CategoriasEntity();
        entity.setIdCategoria(20L);
        entity.setNombre("Piedra");
        entity.setSeccion("Exterior");
        entity.setEstado(true);
        entity.setCliente(cliente);

        when(categoriesRepository.getCategoryById(20L)).thenReturn(Optional.of(entity));

        // Act
        Optional<CategoriesDto> result = categoriesService.getCategoryById(20L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Piedra", result.get().getNombre());
        verify(categoriesRepository, times(1)).getCategoryById(20L);
    }

    @Test
    void testDeleteCategory() {
        // Act
        categoriesService.deleteByIdCategory(15L);

        // Assert
        verify(categoriesRepository, times(1)).deleteByIdCategory(15L);
    }

    @Test
    void testGetCategoriesByIdCliente() {
        // Arrange
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(4L);

        CategoriasEntity entity = new CategoriasEntity();
        entity.setIdCategoria(30L);
        entity.setNombre("Mármol");
        entity.setSeccion("Lujo");
        entity.setEstado(true);
        entity.setCliente(cliente);

        when(categoriesRepository.getCategoriesByIdCliente(4L)).thenReturn(List.of(entity));

        // Act
        List<CategoriesDto> result = categoriesService.getCategoriesByIdCliente(4L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Mármol", result.get(0).getNombre());
        assertEquals(4L, result.get(0).getIdCliente());
        verify(categoriesRepository, times(1)).getCategoriesByIdCliente(4L);
    }
}
