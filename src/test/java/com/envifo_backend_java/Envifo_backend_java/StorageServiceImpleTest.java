package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.StorageDetailsService;
import com.envifo_backend_java.Envifo_backend_java.application.service.StorageServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.AlmacenamientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceImpleTest {

    @Mock
    private StorageDetailsService storageDetailsService;

    @Mock
    private AlmacenamientoRepository almacenamientoRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private StorageServiceImple storageService;

    @BeforeEach
    void setUp() throws IOException {
        // Stubbings lenientes para evitar UnnecessaryStubbingException
        lenient().when(multipartFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("test".getBytes()));
        lenient().when(multipartFile.getSize())
                .thenReturn((long) "test".getBytes().length);
        lenient().when(multipartFile.getContentType())
                .thenReturn("image/png");
        lenient().when(multipartFile.getOriginalFilename())
                .thenReturn("imagen.png");
    }

    @Test
    void saveFile_ShouldReturnStorageDto() throws IOException {
        when(almacenamientoRepository.existsByIdEntidadAndTipoEntidad(anyLong(), anyString()))
                .thenReturn(false);

        AlmacenamientoEntity savedEntity = new AlmacenamientoEntity();
        savedEntity.setIdArchivo(1L);
        savedEntity.setNombreArchivo("imagen.png");
        savedEntity.setIdEntidad(10L);
        savedEntity.setLlaveR2("folder/uuid-imagen.png");

        when(almacenamientoRepository.saveFile(any(AlmacenamientoEntity.class)))
                .thenReturn(savedEntity);

        StorageDto dto = new StorageDto();
        dto.setIdEntity(10L);

        StorageDto result = storageService.saveFile(multipartFile, dto, "tipoEntidad", "folder");

        assertNotNull(result);
        assertEquals(savedEntity.getIdArchivo(), result.getIdFile());
        assertEquals(savedEntity.getNombreArchivo(), result.getNameFile());

        verify(storageDetailsService, times(1)).uploadImage(any(), anyString());
    }

    @Test
    void updateFile_ShouldReturnUpdatedDto() throws IOException {
        AlmacenamientoEntity existing = new AlmacenamientoEntity();
        existing.setIdArchivo(1L);
        existing.setNombreArchivo("old.png");
        existing.setLlaveR2("folder/old.png");
        existing.setIdEntidad(10L);

        when(almacenamientoRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(almacenamientoRepository.saveFile(any(AlmacenamientoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StorageDto result = storageService.updateFile(1L, multipartFile, "folder");

        assertNotNull(result);
        assertEquals("imagen.png", result.getNameFile());
        assertTrue(result.getKeyR2().contains("folder/"));

        verify(storageDetailsService, times(1)).deleteImage(anyString());
        verify(storageDetailsService, times(1)).uploadImage(any(), anyString());
    }

    @Test
    void deleteFileById_ShouldCallDeleteMethods() throws IOException {
        AlmacenamientoEntity existing = new AlmacenamientoEntity();
        existing.setIdArchivo(1L);
        existing.setLlaveR2("folder/file.png");

        when(almacenamientoRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        storageService.deleteFileById(1L);

        verify(storageDetailsService, times(1)).deleteImage("folder/file.png");
        verify(almacenamientoRepository, times(1)).deleteFileById(1L);
    }

    @Test
    void deleteFileByEntity_ShouldCallDeleteMethods() throws IOException {
        AlmacenamientoEntity existing = new AlmacenamientoEntity();
        existing.setIdArchivo(1L);
        existing.setLlaveR2("folder/file.png");

        when(almacenamientoRepository.findByIdEntidadAndTipoEntidad(10L, "tipo"))
                .thenReturn(Optional.of(existing));

        storageService.deleteFileByEntity(10L, "tipo");

        verify(storageDetailsService, times(1)).deleteImage("folder/file.png");
        verify(almacenamientoRepository, times(1)).deleteFileById(1L);
    }

    @Test
    void getPresignedUrl_ShouldReturnUrl() throws IOException {
        AlmacenamientoEntity existing = new AlmacenamientoEntity();
        existing.setIdArchivo(1L);
        existing.setLlaveR2("folder/file.png");

        when(almacenamientoRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(storageDetailsService.generatePresignedUrl("folder/file.png", Duration.ofMinutes(15)))
                .thenReturn("https://example.com/file.png");

        String url = storageService.getPresignedUrl(1L);

        assertEquals("https://example.com/file.png", url);
    }
}
