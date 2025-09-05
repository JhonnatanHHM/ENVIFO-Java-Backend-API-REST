package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.application.service.MaterialsServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.MaterialsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.TexturesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerMaterialRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.TexturesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialsServiceImplTest {

    @Mock
    private MaterialsRepository materialsRepository;
    @Mock
    private CategoriesRepository categoriesRepository;
    @Mock
    private TexturesService texturesService;
    @Mock
    private TexturesRepository texturesRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private StorageRepository storageRepository;
    @Mock
    private MultipartFile mockFile;


    @InjectMocks
    private MaterialsServiceImpl materialsService;

    private MaterialDto materialDto;
    private MaterialesEntity materialEntity;
    private CategoriasEntity categoria;
    private TexturasEntity textura;
    private AlmacenamientoEntity almacenamiento;
    private AlmacenamientoEntity almacenamientoGlb;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new CategoriasEntity();
        categoria.setIdCategoria(1L);

        textura = new TexturasEntity();
        textura.setIdTextura(1L);

        materialDto = new MaterialDto();
        materialDto.setNameMaterial("Madera");
        materialDto.setDescripcionMate("Robusta");
        materialDto.setHeight(BigDecimal.valueOf(60));
        materialDto.setWidth(BigDecimal.valueOf(30));
        materialDto.setStatus(true);
        materialDto.setIdCategoria(1L);
        materialDto.setIdTextura(1L);

        materialEntity = new MaterialesEntity();
        materialEntity.setIdMaterial(100L);
        materialEntity.setNombre("Madera");
        materialEntity.setDescripcionMate("Robusta");
        materialEntity.setAlto(BigDecimal.valueOf(60));
        materialEntity.setAncho(BigDecimal.valueOf(30));
        materialEntity.setEstado(true);
        materialEntity.setCategoria(categoria);
        materialEntity.setTextura(textura);

        almacenamiento = new AlmacenamientoEntity();
        almacenamiento.setIdArchivo(500L);
        almacenamiento.setNombreArchivo("archivo.png");
        almacenamiento.setIdEntidad(100L);

        almacenamientoGlb = new AlmacenamientoEntity();
        almacenamientoGlb.setIdArchivo(600L);
        almacenamientoGlb.setNombreArchivo("archivo.glb");
        almacenamientoGlb.setIdEntidad(100L);

        when(mockFile.isEmpty()).thenReturn(false);
    }

    @Test
    void testSaveMaterial_success() throws IOException {
        when(categoriesRepository.getCategoryById(1L)).thenReturn(Optional.of(categoria));
        when(texturesRepository.getByIdTexture(1L)).thenReturn(Optional.of(textura));
        when(materialsRepository.saveMaterial(any())).thenReturn(materialEntity);

        String result = materialsService.saveMaterial(materialDto, mockFile, mockFile);

        assertEquals("Material creado correctamente", result);
        verify(storageService, times(2)).saveFile(any(), any(), anyString(), anyString());
    }

    @Test
    void testSaveMaterial_categoryNotFound() {
        when(categoriesRepository.getCategoryById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> materialsService.saveMaterial(materialDto, null, null));
    }

    @Test
    void testUpdateMaterial_success() throws IOException {
        when(materialsRepository.getByIdMaterial(100L)).thenReturn(Optional.of(materialEntity));
        when(categoriesRepository.getCategoryById(1L)).thenReturn(Optional.of(categoria));
        when(materialsRepository.saveMaterial(any())).thenReturn(materialEntity);

        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales3d"))
                .thenReturn(Optional.of(almacenamientoGlb));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales"))
                .thenReturn(Optional.of(almacenamiento));

        String result = materialsService.updateMaterial(100L, materialDto, mockFile, mockFile);

        assertEquals("Material actualizado correctamente", result);
        verify(storageService, times(2)).saveFile(any(), any(), anyString(), anyString());
        verify(storageService, times(2)).deleteFileById(anyLong());
    }

    @Test
    void testUpdateMaterial_notFound() {
        when(materialsRepository.getByIdMaterial(200L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> materialsService.updateMaterial(200L, materialDto, null, null));
    }

    @Test
    void testGetByIdMaterial_success() throws IOException {
        when(materialsRepository.getByIdMaterial(100L)).thenReturn(Optional.of(materialEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales")).thenReturn(Optional.of(almacenamiento));
        when(texturesService.getTextureByIdTexture(1L)).thenReturn(Optional.of(new TextureCompleteDto()));
        when(storageService.getPresignedUrl(500L)).thenReturn("url");

        Optional<MaterialCompleteDto> result = materialsService.getByIdMaterial(100L);

        assertTrue(result.isPresent());
        assertEquals("Madera", result.get().getNameMaterial());
    }

    @Test
    void testGetMaterial3dByIdMaterial_success() throws IOException {
        when(materialsRepository.getByIdMaterial(100L)).thenReturn(Optional.of(materialEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales3d")).thenReturn(Optional.of(almacenamiento));
        when(texturesService.getDisplacementByIdTexture(1L)).thenReturn(Optional.of(new TextureCompleteDto()));
        when(storageService.getPresignedUrl(500L)).thenReturn("url");

        Optional<MaterialCompleteDto> result = materialsService.getMaterial3dByIdMaterial(100L);

        assertTrue(result.isPresent());
        assertEquals(100L, result.get().getIdMaterial());
    }

    @Test
    void testGetLastMaterialByCustomer_success() throws IOException {
        when(materialsRepository.getLastMaterialByCustomer(1L)).thenReturn(materialEntity);
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales"))
                .thenReturn(Optional.of(almacenamiento));
        when(texturesService.getTextureByIdTexture(1L))
                .thenReturn(Optional.of(new TextureCompleteDto()));
        when(storageService.getPresignedUrl(500L)).thenReturn("url");

        Optional<MaterialCompleteDto> result = materialsService.getLastMaterialByCustomer(1L);

        assertTrue(result.isPresent());  // ahora sí debería ser true
        assertEquals("Madera", result.get().getNameMaterial());
    }


    @Test
    void testGetLastMaterialByCustomer_notFound() {
        // Simular repo devolviendo null
        when(materialsRepository.getLastMaterialByCustomer(99L)).thenReturn(null);

        Optional<MaterialCompleteDto> result = materialsService.getLastMaterialByCustomer(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteMaterial_success() throws IOException {
        when(materialsRepository.getByIdMaterial(100L)).thenReturn(Optional.of(materialEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales3d")).thenReturn(Optional.of(almacenamiento));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "materiales")).thenReturn(Optional.of(almacenamiento));

        materialsService.deleteMaterial(100L);

        verify(storageService, times(2)).deleteFileById(anyLong());
        verify(materialsRepository).deleteMaterial(100L);
    }

    @Test
    void testDeleteMaterial_notFound() {
        when(materialsRepository.getByIdMaterial(200L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> materialsService.deleteMaterial(200L));
    }
}
