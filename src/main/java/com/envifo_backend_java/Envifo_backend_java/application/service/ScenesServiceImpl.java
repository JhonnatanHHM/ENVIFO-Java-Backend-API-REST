package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.EscenariosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ScenesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.ScenesService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScenesServiceImpl implements ScenesService {

    private final ScenesRepository scenesRepository;
    private final CategoriesRepository categoriesRepository;
    private final StorageRepository storageRepository;
    private final StorageService storageService;

    @Autowired
    public ScenesServiceImpl(ScenesRepository scenesRepository, CategoriesRepository categoriesRepository,
                             StorageRepository storageRepository, StorageService storageService) {
        this.scenesRepository = scenesRepository;
        this.categoriesRepository = categoriesRepository;
        this.storageRepository = storageRepository;
        this.storageService = storageService;
    }

    @Override
    public String saveScene(SceneDto sceneDto, MultipartFile scene,MultipartFile imagen) throws IOException {
        EscenariosEntity newScene = new EscenariosEntity();
        newScene.setNombreEscenario(sceneDto.getSceneName());
        newScene.setDescripcion(sceneDto.getDescription());
        newScene.setEstado(sceneDto.isStatus());
        newScene.setMetadata(sceneDto.getMetadata());

        CategoriasEntity category = categoriesRepository.getCategoryById(sceneDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        newScene.setCategoria(category);

        EscenariosEntity sceneSaved = scenesRepository.saveScene(newScene);

        // Procesar objeto3d si se envía
        if (scene != null && !scene.isEmpty()) {

            StorageDto dto = new StorageDto();
            dto.setIdEntity(sceneSaved.getIdEscenario());

            storageService.saveFile(scene, dto, "escenas", "escenas");
        }

        // Procesar imagen si se envía
        if (imagen != null && !imagen.isEmpty()) {

            StorageDto dto = new StorageDto();
            dto.setIdEntity(sceneSaved.getIdEscenario());

            storageService.saveFile(imagen, dto, "escenas_img", "imagenes");
        }

        return "Escena creada correctamente";
    }

    @Override
    public Optional<SceneCompleteDto> getScenedByIdScene(Long idScene) {
        return getSceneDto(idScene, "escenas");
    }

    @Override
    public Optional<SceneCompleteDto> getSceneImgByIdTexture(Long idScene) {
        return getSceneDto(idScene, "escenas_img");
    }

    @Override
    public List<SceneCompleteDto> getByNameAndSectionCategory(String nameCategory, String section) {
        return scenesRepository.getSceneByNameAndSectionCategory(nameCategory, section)
                .stream()
                .map(scene -> {
                    Optional<AlmacenamientoEntity> imgOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(scene.getIdEscenario(), "escenas_img");
                    return imgOpt.map(img -> convertToSceneDto(scene, img));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteScene(Long idScene) throws IOException {
        Optional<AlmacenamientoEntity> sceneOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idScene, "escenas");

        Optional<AlmacenamientoEntity> sceneImageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idScene, "escenas_img");

        Optional<EscenariosEntity> escenaOpt = scenesRepository.getByIdScene(idScene);

        // Validar existencia
        if (escenaOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la escena con ID: " + idScene);
        }

        // Eliminar archivos si existen
        sceneOpt.ifPresent(object -> {
            try {
                storageService.deleteFileById(object.getIdArchivo());
            } catch (IOException e) {
                // log error si usas loggers
                System.err.println("Error al eliminar archivo de la escena: " + e.getMessage());
            }
        });

        sceneImageOpt.ifPresent(image -> {
            try {
                storageService.deleteFileById(image.getIdArchivo());
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen asociada a la escena: " + e.getMessage());
            }
        });

        escenaOpt.ifPresent(scene -> scenesRepository.deleteScene(scene.getIdEscenario()));
    }

    private Optional<SceneCompleteDto> getSceneDto(Long idScene, String entity) {
        Optional<EscenariosEntity> sceneOpt = scenesRepository.getByIdScene(idScene);

        if (sceneOpt.isEmpty()) {
            return Optional.empty();
        }

        EscenariosEntity scene = sceneOpt.get();

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(scene.getIdEscenario(), entity);

        if (imageOpt.isEmpty()) {
            return Optional.empty();
        }

        SceneCompleteDto dto = convertToSceneDto(scene, imageOpt.get());
        return Optional.of(dto);
    }

    private SceneCompleteDto convertToSceneDto(EscenariosEntity scene, AlmacenamientoEntity imagen) {
        SceneCompleteDto dto = new SceneCompleteDto();
        dto.setIdScene(scene.getIdEscenario());
        dto.setSceneName(scene.getNombreEscenario());
        dto.setDescription(scene.getDescripcion());
        dto.setStatus(scene.isEstado());
        dto.setMetadata(scene.getMetadata());
        dto.setImage(convertToStorageDto(imagen).orElse(null));
        return dto;
    }

    private Optional<StorageDto> convertToStorageDto(AlmacenamientoEntity imagen) {
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
