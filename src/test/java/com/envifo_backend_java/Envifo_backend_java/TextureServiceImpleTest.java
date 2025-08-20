package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TextureCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TexturesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.TexturasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.CategoriasRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.TexturasRepository;
import com.envifo_backend_java.Envifo_backend_java.application.service.TextureServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextureServiceImpleTest {

    @Mock
    private TexturasRepository texturasRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private CategoriasRepository categoriasRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private TextureServiceImple textureService;

    private TexturasEntity textureEntity;
    private AlmacenamientoEntity storageEntity;

    @BeforeEach
    void setUp() throws IOException {
        textureEntity = new TexturasEntity();
        textureEntity.setIdTextura(1L);
        textureEntity.setNombreTextura("Marmol");
        textureEntity.setDescripcion("Textura de marmol");

        storageEntity = new AlmacenamientoEntity();
        storageEntity.setIdArchivo(10L);
        storageEntity.setNombreArchivo("file.png");
        storageEntity.setIdEntidad(1L);

        // lenient para MultipartFile
        lenient().when(multipartFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("data".getBytes()));
        lenient().when(multipartFile.getOriginalFilename()).thenReturn("file.png");
        lenient().when(multipartFile.getSize()).thenReturn((long) "data".getBytes().length);
        lenient().when(multipartFile.isEmpty()).thenReturn(false);
    }

    @Test
    void getTextureByIdTexture_ShouldReturnDto() throws IOException {
        when(texturasRepository.getByIdTexture(1L)).thenReturn(Optional.of(textureEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(1L, "texturas"))
                .thenReturn(Optional.of(storageEntity));
        when(storageService.getPresignedUrl(10L)).thenReturn("https://example.com/file.png");

        Optional<TextureCompleteDto> result = textureService.getTextureByIdTexture(1L);

        assertTrue(result.isPresent());
        assertEquals("Marmol", result.get().getNameTexture());
        assertEquals("https://example.com/file.png", result.get().getImage().get().getKeyR2());
    }

    @Test
    void getDisplacementByIdTexture_ShouldReturnDto() throws IOException {
        when(texturasRepository.getByIdTexture(1L)).thenReturn(Optional.of(textureEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(1L, "displacements"))
                .thenReturn(Optional.of(storageEntity));
        when(storageService.getPresignedUrl(10L)).thenReturn("https://example.com/file.png");

        Optional<TextureCompleteDto> result = textureService.getDisplacementByIdTexture(1L);

        assertTrue(result.isPresent());
        assertEquals("Marmol", result.get().getNameTexture());
    }

    @Test
    void getByNameAndSectionCategory_ShouldReturnList() throws IOException {
        List<TexturasEntity> textures = Collections.singletonList(textureEntity);
        when(texturasRepository.getByNameAndSectionCategory("Marmol", "texturas"))
                .thenReturn(textures);
        when(storageRepository.findByIdEntidadAndTipoEntidad(1L, "texturas"))
                .thenReturn(Optional.of(storageEntity));
        when(storageService.getPresignedUrl(10L)).thenReturn("https://example.com/file.png");

        List<TextureCompleteDto> result = textureService.getByNameAndSectionCategory("Marmol", "texturas");

        assertEquals(1, result.size());
        assertEquals("Marmol", result.get(0).getNameTexture());
    }

    @Test
    void saveTexture_ShouldCallStorageService() throws IOException {
        TexturesDto dto = new TexturesDto();
        dto.setNameTexture("Marmol");
        dto.setDescription("Textura de marmol");
        dto.setIdCategory(5L);

        CategoriasEntity category = new CategoriasEntity();
        category.setIdCategoria(5L);
        when(categoriasRepository.getCategoryById(5L)).thenReturn(Optional.of(category));

        when(texturasRepository.saveTexture(any(TexturasEntity.class))).thenAnswer(inv -> {
            TexturasEntity tex = inv.getArgument(0);
            tex.setIdTextura(1L);
            return tex;
        });

        String result = textureService.saveTexture(dto, multipartFile, multipartFile);

        assertEquals("Textura creada correctamente", result);
        verify(storageService, times(2)).saveFile(any(), any(), anyString(), anyString());
    }

    @Test
    void deleteTexture_ShouldCallDeleteAndRepositoryMethods() throws IOException {
        when(storageRepository.findByIdEntidadAndTipoEntidad(1L, "displacements"))
                .thenReturn(Optional.of(storageEntity));
        when(storageRepository.findByIdEntidadAndTipoEntidad(1L, "texturas"))
                .thenReturn(Optional.of(storageEntity));
        when(texturasRepository.getByIdTexture(1L)).thenReturn(Optional.of(textureEntity));

        textureService.deleteTexture(1L);

        verify(storageService, times(2)).deleteFileById(anyLong());
        verify(texturasRepository, times(1)).deleteTexture(1L);
    }
}
