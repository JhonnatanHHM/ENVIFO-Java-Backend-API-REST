package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.ScenesServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.EscenariosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ScenesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScenesServiceImplTest {

    @Mock
    private ScenesRepository scenesRepository;

    @Mock
    private CategoriesRepository categoriesRepository;

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ScenesServiceImpl scenesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveScene_ShouldSaveSceneAndFiles() throws IOException {
        SceneDto dto = new SceneDto();
        dto.setSceneName("Escena Test");
        dto.setDescription("Desc");
        dto.setStatus(true);
        dto.setMetadata("meta");
        dto.setCategoryId(1L);

        CategoriasEntity categoria = new CategoriasEntity();
        categoria.setIdCategoria(1L);

        EscenariosEntity escenarioSaved = new EscenariosEntity();
        escenarioSaved.setIdEscenario(100L);

        MultipartFile sceneFile = mock(MultipartFile.class);
        MultipartFile imgFile = mock(MultipartFile.class);

        when(categoriesRepository.getCategoryById(1L)).thenReturn(Optional.of(categoria));
        when(scenesRepository.saveScene(any(EscenariosEntity.class))).thenReturn(escenarioSaved);
        when(sceneFile.isEmpty()).thenReturn(false);
        when(imgFile.isEmpty()).thenReturn(false);

        String result = scenesService.saveScene(dto, sceneFile, imgFile);

        assertEquals("Escena creada correctamente", result);
        verify(storageService, times(1)).saveFile(eq(sceneFile), any(StorageDto.class), eq("escenas"), eq("escenas"));
        verify(storageService, times(1)).saveFile(eq(imgFile), any(StorageDto.class), eq("escenas_img"), eq("imagenes"));
    }

    @Test
    void getScenedByIdScene_ShouldReturnSceneCompleteDto() throws IOException {
        EscenariosEntity escena = new EscenariosEntity();
        escena.setIdEscenario(10L);
        escena.setNombreEscenario("EscenaX");
        escena.setDescripcion("desc");
        escena.setEstado(true);
        escena.setMetadata("meta");

        AlmacenamientoEntity almacenamiento = new AlmacenamientoEntity();
        almacenamiento.setIdArchivo(500L);
        almacenamiento.setNombreArchivo("archivo.glb");
        almacenamiento.setIdEntidad(10L);

        when(scenesRepository.getByIdScene(10L)).thenReturn(Optional.of(escena));
        when(storageRepository.findByIdEntidadAndTipoEntidad(10L, "escenas")).thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(500L)).thenReturn("http://url-presigned");

        Optional<SceneCompleteDto> result = scenesService.getScenedByIdScene(10L);

        assertTrue(result.isPresent());
        assertEquals("EscenaX", result.get().getSceneName());
        assertNotNull(result.get().getImage());
        assertEquals("http://url-presigned", result.get().getImage().getKeyR2());
    }

    @Test
    void getByNameAndSectionCategory_ShouldReturnList() throws IOException {
        EscenariosEntity escena = new EscenariosEntity();
        escena.setIdEscenario(20L);
        escena.setNombreEscenario("EscenaY");

        AlmacenamientoEntity img = new AlmacenamientoEntity();
        img.setIdArchivo(500L);
        img.setNombreArchivo("imagen.png");
        img.setIdEntidad(20L);

        when(scenesRepository.getSceneByNameAndSectionCategory("Cat1", "Sec1"))
                .thenReturn(List.of(escena));
        when(storageRepository.findByIdEntidadAndTipoEntidad(20L, "escenas_img"))
                .thenReturn(Optional.of(img));
        when(storageService.getPresignedUrl(500L)).thenReturn("http://url-img");

        List<SceneCompleteDto> result = scenesService.getByNameAndSectionCategory("Cat1", "Sec1");

        assertEquals(1, result.size());
        assertEquals("EscenaY", result.get(0).getSceneName());
        assertEquals("http://url-img", result.get(0).getImage().getKeyR2());
    }

    @Test
    void deleteScene_ShouldDeleteSceneAndFiles() throws IOException {
        EscenariosEntity escena = new EscenariosEntity();
        escena.setIdEscenario(30L);

        AlmacenamientoEntity archivo = new AlmacenamientoEntity();
        archivo.setIdArchivo(400L);

        AlmacenamientoEntity img = new AlmacenamientoEntity();
        img.setIdArchivo(500L);

        when(scenesRepository.getByIdScene(30L)).thenReturn(Optional.of(escena));
        when(storageRepository.findByIdEntidadAndTipoEntidad(30L, "escenas")).thenReturn(Optional.of(archivo));
        when(storageRepository.findByIdEntidadAndTipoEntidad(30L, "escenas_img")).thenReturn(Optional.of(img));

        scenesService.deleteScene(30L);

        verify(storageService, times(1)).deleteFileById(400L);
        verify(storageService, times(1)).deleteFileById(500L);
        verify(scenesRepository, times(1)).deleteScene(30L);
    }

    @Test
    void deleteScene_ShouldThrowIfNotFound() {
        when(scenesRepository.getByIdScene(40L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> scenesService.deleteScene(40L));

        assertEquals("No se encontró la escena con ID: 40", ex.getMessage());
    }
}
