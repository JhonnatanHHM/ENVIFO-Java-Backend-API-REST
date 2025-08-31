package com.envifo_backend_java.Envifo_backend_java.infrastructure.messaging;

import com.envifo_backend_java.Envifo_backend_java.application.dto.MaterialDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.MaterialListenerDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.StorageDetailsService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.MaterialsService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.MultipertFile.InMemoryMultipartFile;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class MaterialMessageListener {

    @Value("${cloudflare.r2.bucket.transition}")
    private String bucketName;

    private final S3Client s3Client;
    private final MaterialsService materialsService;
    private final ObjectMapper objectMapper;
    private final StorageDetailsService storageService;

    @Autowired
    public MaterialMessageListener(S3Client s3Client,
                                   MaterialsService materialsService,
                                   ObjectMapper objectMapper,
                                   StorageDetailsService storageService) {
        this.s3Client = s3Client;
        this.materialsService = materialsService;
        this.objectMapper = objectMapper;
        this.storageService = storageService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(String message) {
        try {
            MaterialListenerDto response = objectMapper.readValue(message, MaterialListenerDto.class);

            // Descargar archivos desde R2 con extensión correcta
            Path imagePath = downloadFromR2(response.getImageUrl());
            Path modelPath = downloadFromR2(response.getModelUrl());

            // Extensiones originales
            String modelExtension = getExtension(response.getModelUrl());
            String imageExtension = getExtension(response.getImageUrl());

            // Detectar tipo MIME real (fallback a octet-stream)
            String modelMimeType = Files.probeContentType(modelPath);
            if (modelMimeType == null) modelMimeType = "application/octet-stream";

            String imageMimeType = Files.probeContentType(imagePath);
            if (imageMimeType == null) imageMimeType = "application/octet-stream";

            // Crear DTO de material
            MaterialDto materialDto = new MaterialDto();
            materialDto.setNameMaterial(response.getMaterial().getNameMaterial());
            materialDto.setDescripcionMate(response.getMaterial().getDescripcionMate());
            materialDto.setHeight(response.getMaterial().getHeight());
            materialDto.setWidth(response.getMaterial().getWidth());
            materialDto.setStatus(response.getMaterial().isStatus());
            materialDto.setIdCategoria(response.getMaterial().getIdCategoria());
            materialDto.setIdTextura(response.getMaterial().getIdTextura());
            materialDto.setIdCliente(response.getMaterial().getIdCliente());

            // Convertir archivos descargados a MultipartFile (implementación propia en memoria)
            MultipartFile modelFile = toMultipartFile(
                    modelPath,
                    "modelo" + materialDto.getNameMaterial() + modelExtension,
                    modelMimeType
            );

            MultipartFile imageFile = toMultipartFile(
                    imagePath,
                    "imagen" + materialDto.getNameMaterial() + imageExtension,
                    imageMimeType
            );

            // Crear / Actualizar
            if (response.getMaterial().getIdMaterial() == null) {
                materialsService.saveMaterial(materialDto, modelFile, imageFile);
            } else {
                Long idMaterial = response.getMaterial().getIdMaterial();
                materialDto.setIdMaterial(idMaterial);
                materialsService.updateMaterial(idMaterial, materialDto, modelFile, imageFile);
            }

            // Eliminar en R2
            deleteImage(extractKeyFromUrl(response.getImageUrl()));
            deleteImage(extractKeyFromUrl(response.getModelUrl()));

            // Limpiar archivos temporales locales
            Files.deleteIfExists(imagePath);
            Files.deleteIfExists(modelPath);

            System.out.println("Material recibido, procesado y eliminado del bucket: " +
                    response.getMaterial().getNameMaterial());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Path downloadFromR2(String fileUrl) throws IOException {
        String key = extractKeyFromUrl(fileUrl);

        // Extensión del archivo
        String extension = "";
        int dotIndex = key.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = key.substring(dotIndex);
        }

        // Archivo temporal con extensión correcta
        Path tempFile = Files.createTempFile("material_", extension);

        try (InputStream inputStream = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build())) {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }

    private String getExtension(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        int dotIndex = key.lastIndexOf(".");
        if (dotIndex > 0) {
            return key.substring(dotIndex); // ".glb", ".jpg", etc.
        }
        return "";
    }

    private String extractKeyFromUrl(String url) {
        if (url == null) return null;
        String bucketPrefix = bucketName + "/";
        int index = url.indexOf(bucketPrefix);
        if (index == -1) {
            throw new RuntimeException("No se pudo extraer la key de la URL: " + url);
        }
        return url.substring(index + bucketPrefix.length());
    }

    private void deleteImage(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteRequest);
    }

    // Convierte un Path a MultipartFile en memoria
    private MultipartFile toMultipartFile(Path filePath, String originalFileName, String contentType) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        return new InMemoryMultipartFile("file", originalFileName, contentType, bytes);
    }

}
