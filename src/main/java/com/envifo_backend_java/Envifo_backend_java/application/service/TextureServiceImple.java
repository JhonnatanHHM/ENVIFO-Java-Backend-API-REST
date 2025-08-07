package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CategoriesDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TextureCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TexturesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.TexturasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.TexturesService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.CategoriasRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.TexturasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TextureServiceImple implements TexturesService {

    private TexturasRepository texturasRepository;

    private StorageService storageService;

    private StorageRepository storageRepository;

    private CategoriasRepository categoriasRepository;

    @Autowired
    public TextureServiceImple(CategoriasRepository categoriasRepository, StorageRepository storageRepository, TexturasRepository texturasRepository, StorageService storageService) {
        this.texturasRepository = texturasRepository;
        this.storageService = storageService;
        this.storageRepository = storageRepository;
        this.categoriasRepository = categoriasRepository;
    }

    @Override
    public Optional<TextureCompleteDto> getDisplacementByIdTexture(Long idTexture) {
        return getTextureDto(idTexture, "displacements");
    }

    @Override
    public Optional<TextureCompleteDto> getTextureByIdTexture(Long idTexture) {
        return getTextureDto(idTexture, "texturas");
    }


    @Override
    public List<TextureCompleteDto> getByNameAndSectionCategory(String nameCategory, String section) {
        List<TexturasEntity> texturas = texturasRepository.getByNameAndSectionCategory(nameCategory, section);

        return texturas.stream()
                .map(textura -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    textura.getIdTextura(), "texturas");

                    return imageOpt.map(image -> convertToTextureDto(textura, image));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public String saveTexture(TexturesDto texture, MultipartFile textura, MultipartFile imagen) throws IOException {

        TexturasEntity newTexture = new TexturasEntity();
        newTexture.setNombreTextura(texture.getNameTexture());
        newTexture.setDescripcion(texture.getDescription());

        CategoriasEntity category = categoriasRepository.getCategoryById(texture.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        newTexture.setCategoria(category);

        TexturasEntity textureSaved = texturasRepository.saveTexture(newTexture);

        // Procesar textura si se envía
        if (textura != null && !textura.isEmpty()) {

                StorageDto dto = new StorageDto();
                dto.setIdEntity(textureSaved.getIdTextura());

                storageService.saveFile(textura, dto, "displacements", "texturas");
            }

        // Procesar imagen si se envía
        if (imagen != null && !imagen.isEmpty()) {

            StorageDto dto = new StorageDto();
            dto.setIdEntity(textureSaved.getIdTextura());

            storageService.saveFile(imagen, dto, "texturas", "imagenes");
        }

        return "Textura creada correctamente";
    }

    @Override
    public void deleteTexture(Long idTexture) throws IOException {

        Optional<AlmacenamientoEntity> textureOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idTexture, "displacements");

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idTexture, "texturas");

        Optional<TexturasEntity> texturaOpt = texturasRepository.getByIdTexture(idTexture);

        // Validar existencia
        if (texturaOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la textura con ID: " + idTexture);
        }

        // Eliminar archivos si existen
        textureOpt.ifPresent(texture -> {
            try {
                storageService.deleteFileById(texture.getIdArchivo());
            } catch (IOException e) {
                // log error si usas loggers
                System.err.println("Error al eliminar archivo de textura: " + e.getMessage());
            }
        });

        imageOpt.ifPresent(image -> {
            try {
                storageService.deleteFileById(image.getIdArchivo());
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen asociada a textura: " + e.getMessage());
            }
        });

        texturaOpt.ifPresent(textura -> texturasRepository.deleteTexture(textura.getIdTextura()));
    }

    private Optional<TextureCompleteDto> getTextureDto(Long idTexture, String entity) {
        Optional<TexturasEntity> textureOpt = texturasRepository.getByIdTexture(idTexture);

        if (textureOpt.isEmpty()) {
            return Optional.empty();
        }

        TexturasEntity texture = textureOpt.get();

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(texture.getIdTextura(), entity);

        if (imageOpt.isEmpty()) {
            return Optional.empty();
        }

        TextureCompleteDto dto = convertToTextureDto(texture, imageOpt.get());
        return Optional.of(dto);
    }


    private TextureCompleteDto convertToTextureDto (TexturasEntity texture, AlmacenamientoEntity imagen) {

        TextureCompleteDto textureDto = new TextureCompleteDto();
        textureDto.setIdTexture(texture.getIdTextura());
        textureDto.setNameTexture(texture.getNombreTextura());
        textureDto.setDescription(texture.getDescripcion());
        textureDto.setImage(convertToStorageDto(imagen));

        return textureDto;
    }

    private Optional<StorageDto> convertToStorageDto (AlmacenamientoEntity imagen) {
        StorageDto storageDto = new StorageDto();
        storageDto.setIdFile(imagen.getIdArchivo());
        storageDto.setNameFile(imagen.getNombreArchivo());
        storageDto.setIdEntity(imagen.getIdEntidad());

        try {
            String url = storageService.getPresignedUrl(imagen.getIdArchivo());
            storageDto.setKeyR2(url);
        } catch (Exception e) {
            storageDto.setKeyR2("Error al generar URL");
        }

        return Optional.of(storageDto);

    }
}
