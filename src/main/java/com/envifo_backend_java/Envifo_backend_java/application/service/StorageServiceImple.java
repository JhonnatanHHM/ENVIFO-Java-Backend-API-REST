package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.AlmacenamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class StorageServiceImple implements StorageService {

    private final StorageDetailsService storageDetailsService;
    private final AlmacenamientoRepository almacenamientoRepository;

    @Autowired
    public StorageServiceImple(StorageDetailsService storageDetailsService, AlmacenamientoRepository almacenamientoRepository) {
        this.storageDetailsService = storageDetailsService;
        this.almacenamientoRepository = almacenamientoRepository;
    }

    @Override
    public StorageDto saveFile(MultipartFile file, StorageDto dto, String tipoEntidad, String folder) throws IOException {

        // ✅ Validar existencia con existsBy en vez de findBy
        if (almacenamientoRepository.existsByIdEntidadAndTipoEntidad(dto.getIdEntity(), tipoEntidad)) {
            throw new IllegalStateException("Ya se encuentra una imagen asociada. Actualiza la imagen para reemplazarla.");
        }

        String nombreOriginal = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "-" + nombreOriginal;
        String key = folder + "/" + fileName;

        // 📤 Subir imagen a R2
        storageDetailsService.uploadImage(file, key);

        // 💾 Guardar en la base de datos
        AlmacenamientoEntity newFile = new AlmacenamientoEntity();
        newFile.setNombreArchivo(nombreOriginal);
        newFile.setLlaveR2(key);
        newFile.setTipoEntidad(tipoEntidad);
        newFile.setIdEntidad(dto.getIdEntity());

        AlmacenamientoEntity savedFile = almacenamientoRepository.saveFile(newFile);

        // 📦 Devolver DTO de respuesta
        StorageDto response = new StorageDto();
        response.setIdFile(savedFile.getIdArchivo());
        response.setNameFile(savedFile.getNombreArchivo());
        response.setKeyR2(""); // ← se llena luego con getPresignedUrl
        response.setIdEntity(savedFile.getIdEntidad());

        return response;
    }


    @Override
    public StorageDto updateFile(Long idArchivo, MultipartFile file) throws IOException {
        AlmacenamientoEntity editFile = almacenamientoRepository.findById(idArchivo)
                .orElseThrow(() -> new IOException("Archivo no encontrado"));

        // Eliminar imagen anterior de R2
        storageDetailsService.deleteImage(editFile.getLlaveR2());

        // Subir nueva imagen
        String nombreOriginal = file.getOriginalFilename();
        String nuevoNombre = UUID.randomUUID() + "-" + nombreOriginal;
        String nuevaLlave = "imagenes/" + nuevoNombre;

        storageDetailsService.uploadImage(file, nuevaLlave);

        // Actualizar entidad
        editFile.setNombreArchivo(nombreOriginal);
        editFile.setLlaveR2(nuevaLlave);

        AlmacenamientoEntity updatedFile = almacenamientoRepository.saveFile(editFile);

        // Construir respuesta DTO
        StorageDto response = new StorageDto();
        response.setIdFile(updatedFile.getIdArchivo());
        response.setNameFile(updatedFile.getNombreArchivo());
        response.setKeyR2(updatedFile.getLlaveR2());
        response.setIdEntity(updatedFile.getIdEntidad());

        return response;
    }

    @Override
    public void deleteFileById(Long idArchivo) throws IOException {
        AlmacenamientoEntity archivo = almacenamientoRepository.findById(idArchivo)
                .orElseThrow(() -> new IOException("Archivo no encontrado"));

        storageDetailsService.deleteImage(archivo.getLlaveR2());

        almacenamientoRepository.deleteFileById(idArchivo);
    }

    @Override
    public void deleteFileByEntity(Long idEntidad, String tipoEntidad) throws IOException {
        AlmacenamientoEntity archivo = almacenamientoRepository.findByIdEntidadAndTipoEntidad(idEntidad, tipoEntidad)
                .orElseThrow(() -> new IOException("Archivo no encontrado"));

        // Eliminar de R2
        storageDetailsService.deleteImage(archivo.getLlaveR2());

        // Eliminar de base de datos
        almacenamientoRepository.deleteFileById(archivo.getIdArchivo());
    }

    // Obtener URL presignada
    @Override
    public String getPresignedUrl(Long idArchivo) throws IOException {
        AlmacenamientoEntity archivo = almacenamientoRepository.findById(idArchivo)
                .orElseThrow(() -> new IOException("Archivo no encontrado"));

        return storageDetailsService.generatePresignedUrl(archivo.getLlaveR2(), Duration.ofMinutes(15));
    }

}
