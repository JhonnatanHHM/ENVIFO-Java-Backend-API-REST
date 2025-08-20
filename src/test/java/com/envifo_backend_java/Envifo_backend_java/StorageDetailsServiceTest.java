package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.service.StorageDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import java.util.function.Consumer;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StorageDetailsServiceTest {

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private StorageDetailsService storageDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inyectamos bucketName usando reflexión (porque @Value no se resuelve en test unitario)
        try {
            java.lang.reflect.Field bucketField = StorageDetailsService.class.getDeclaredField("bucketName");
            bucketField.setAccessible(true);
            bucketField.set(storageDetailsService, "test-bucket");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void uploadImage_ShouldCallPutObject() throws IOException {
        // Arrange
        byte[] content = "test data".getBytes();
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(multipartFile.getSize()).thenReturn((long) content.length);
        when(multipartFile.getContentType()).thenReturn("image/png");

        // Act
        storageDetailsService.uploadImage(multipartFile, "test-key");

        // Assert
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void generatePresignedUrl_ShouldReturnUrl() throws Exception {
        String expectedUrl = "https://s3.amazonaws.com/test-bucket/test-key";

        PresignedGetObjectRequest mockPresignedRequest = mock(PresignedGetObjectRequest.class);
        when(mockPresignedRequest.url()).thenReturn(new URL(expectedUrl));

        // Resolver ambigüedad de sobrecarga
        when(s3Presigner.presignGetObject((Consumer<GetObjectPresignRequest.Builder>) any()))
                .thenReturn(mockPresignedRequest);

        String result = storageDetailsService.generatePresignedUrl("test-key", Duration.ofMinutes(5));

        assertEquals(expectedUrl, result);
    }



    @Test
    void deleteImage_ShouldCallDeleteObject() {
        // Act
        storageDetailsService.deleteImage("test-key");

        // Assert
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    // Helper para mockear URL
    private URL newURL(String url) {
        try {
            return new URL(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

