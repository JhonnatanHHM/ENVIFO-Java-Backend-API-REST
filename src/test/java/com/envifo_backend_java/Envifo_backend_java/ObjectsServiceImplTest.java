package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ObjetosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ObjectsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.application.service.ObjectsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectsServiceImplTest {

    @Mock
    private ObjectsRepository objectsRepository;
    @Mock
    private CategoriesRepository categoriesRepository;
    @Mock
    private StorageRepository storageRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private MultipartFile mockFile;

    @InjectMocks
    private ObjectsServiceImpl objectsService;

    private ObjectDto objectDto;
    private ObjetosEntity objetoEntity;
    private CategoriasEntity categoria;
    private AlmacenamientoEntity almacenamiento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new CategoriasEntity();
        categoria.setIdCategoria(1L);

        objectDto = new ObjectDto();
        objectDto.setObjectName("Silla");
        objectDto.setHeight(BigDecimal.valueOf(50));
        objectDto.setWidth(BigDecimal.valueOf(30));
        objectDto.setDepth(BigDecimal.valueOf(40));
        objectDto.setStatus(true);
        objectDto.setCategoryId(1L);

        objetoEntity = new ObjetosEntity();
        objetoEntity.setIdObjeto(100L);
        objetoEntity.setNombreObjeto("Silla");
        objetoEntity.setAlto(BigDecimal.valueOf(50));
        objetoEntity.setAncho(BigDecimal.valueOf(30));
        objetoEntity.setProfundidad(BigDecimal.valueOf(40));
        objetoEntity.setEstado(true);
        objetoEntity.setCategoria(categoria);

        almacenamiento = new AlmacenamientoEntity();
        almacenamiento.setIdArchivo(200L);
        almacenamiento.setNombreArchivo("archivo.glb");
        almacenamiento.setIdEntidad(100L);

        when(mockFile.isEmpty()).thenReturn(false);
    }

    @Test
    void testSaveObject_success() throws IOException {
        when(categoriesRepository.getCategoryById(1L)).thenReturn(Optional.of(categoria));
        when(objectsRepository.saveObject(any())).thenReturn(objetoEntity);

        String result = objectsService.saveObject(objectDto, mockFile, mockFile);

        assertEquals("Objeto creado correctamente", result);
        verify(storageService, times(2)).saveFile(any(), any(), anyString(), anyString());
    }

    @Test
    void testSaveObject_categoryNotFound() {
        when(categoriesRepository.getCategoryById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> objectsService.saveObject(objectDto, null, null));
    }

    @Test
    void testGetObjetc3dByIdObject_success() throws Exception {
        when(objectsRepository.getByIdObject(100L)).thenReturn(Optional.of(objetoEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "objetos3d")).thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(200L)).thenReturn("url");

        Optional<ObjectCompleteDto> result = objectsService.getObjetc3dByIdObject(100L);

        assertTrue(result.isPresent());
        assertEquals("Silla", result.get().getObjectName());
        assertEquals(100L, result.get().getObjectId());
    }

    @Test
    void testGetObjectImgByIdTexture_success() throws Exception {
        when(objectsRepository.getByIdObject(100L)).thenReturn(Optional.of(objetoEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "objetos")).thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(200L)).thenReturn("url");

        Optional<ObjectCompleteDto> result = objectsService.getObjectImgByIdTexture(100L);

        assertTrue(result.isPresent());
        assertEquals("Silla", result.get().getObjectName());
    }

    @Test
    void testGetByNameAndSectionCategory_success() throws Exception {
        when(objectsRepository.getObjectByNameAndSectionCategory("Muebles", "Sala"))
                .thenReturn(Collections.singletonList(objetoEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "objetos"))
                .thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(200L)).thenReturn("url");

        List<ObjectCompleteDto> result = objectsService.getByNameAndSectionCategory("Muebles", "Sala");

        assertEquals(1, result.size());
        assertEquals("Silla", result.get(0).getObjectName());
    }

    @Test
    void testGetObjectsByIdsObjects_success() throws Exception {
        when(objectsRepository.getObjectsByIdsObjects(List.of(100L)))
                .thenReturn(Collections.singletonList(objetoEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "objetos3d"))
                .thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(200L)).thenReturn("url");

        List<ObjectCompleteDto> result = objectsService.getObjectsByIdsObjects(List.of(100L));

        assertEquals(1, result.size());
        assertEquals("Silla", result.get(0).getObjectName());
    }

    @Test
    void testDeleteObject_success() throws Exception {
        when(objectsRepository.getByIdObject(100L)).thenReturn(Optional.of(objetoEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "3d"))
                .thenReturn(Optional.of(almacenamiento));
        when(storageRepository.findByIdEntidadAndTipoEntidad(100L, "objetos"))
                .thenReturn(Optional.of(almacenamiento));

        objectsService.deleteObject(100L);

        verify(storageService, times(2)).deleteFileById(anyLong());
        verify(objectsRepository).deleteObject(100L);
    }

    @Test
    void testDeleteObject_notFound() {
        when(objectsRepository.getByIdObject(200L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> objectsService.deleteObject(200L));
    }
}

