package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    StorageDto saveFile(MultipartFile file, StorageDto dto, String tipoEntidad, String folder) throws IOException;
    StorageDto updateFile(Long idArchivo, MultipartFile file, String folder) throws IOException;
    void deleteFileById(Long idArchivo) throws IOException;
    void deleteFileByEntity(Long idEntidad, String tipoEntidad) throws IOException;
    String getPresignedUrl(Long idArchivo) throws IOException;
}
