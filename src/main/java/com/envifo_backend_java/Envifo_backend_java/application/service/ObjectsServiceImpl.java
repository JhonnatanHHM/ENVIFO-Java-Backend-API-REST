package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ObjetosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ObjectsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.StorageRepository;


import com.envifo_backend_java.Envifo_backend_java.domain.service.ObjectsService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObjectsServiceImpl implements ObjectsService {

    private final ObjectsRepository objectsRepository;
    private final CategoriesRepository categoriesRepository;
    private final StorageRepository storageRepository;
    private final StorageService storageService;

    @Autowired
    public ObjectsServiceImpl(ObjectsRepository objectsRepository, CategoriesRepository categoriesRepository, StorageRepository storageRepository, StorageService storageService) {
        this.objectsRepository = objectsRepository;
        this.categoriesRepository = categoriesRepository;
        this.storageRepository = storageRepository;
        this.storageService = storageService;
    }

    @Override
    public String saveObject(ObjectDto objectDto, MultipartFile objeto3d, MultipartFile imagen) throws IOException {

        ObjetosEntity newObject = new ObjetosEntity();
        newObject.setNombreObjeto(objectDto.getObjectName());
        newObject.setAlto(objectDto.getHeight());
        newObject.setAncho(objectDto.getWidth());
        newObject.setProfundidad(objectDto.getDepth());
        newObject.setEstado(objectDto.isStatus());

        CategoriasEntity category = categoriesRepository.getCategoryById(objectDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        newObject.setCategoria(category);

        ObjetosEntity objectSaved = objectsRepository.saveObject(newObject);

        // Procesar objeto3d si se envía
        if (objeto3d != null && !objeto3d.isEmpty()) {

            StorageDto dto = new StorageDto();
            dto.setIdEntity(objectSaved.getIdObjeto());

            storageService.saveFile(objeto3d, dto, "objetos3d", "3d");
        }

        // Procesar imagen si se envía
        if (imagen != null && !imagen.isEmpty()) {

            StorageDto dto = new StorageDto();
            dto.setIdEntity(objectSaved.getIdObjeto());

            storageService.saveFile(imagen, dto, "objetos", "imagenes");
        }

        return "Objeto creado correctamente";
    }

    @Override
    public Optional<ObjectCompleteDto> getObjetc3dByIdObject(Long idObject) {
        return getObjectDto(idObject, "objetos3d");
    }

    @Override
    public Optional<ObjectCompleteDto> getObjectImgByIdTexture(Long idObject) {
        return getObjectDto(idObject, "objetos");
    }


    @Override
    public List<ObjectCompleteDto> getByNameAndSectionCategory(String nameCategory, String section) {
        List<ObjetosEntity> objetos = objectsRepository.getObjectByNameAndSectionCategory(nameCategory, section);

        return objetos.stream()
                .map(objeto -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    objeto.getIdObjeto(), "objetos");

                    return imageOpt.map(image -> convertToObjectDto(objeto, image));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<ObjectCompleteDto> getObjectsByIdsObjects(List<Long> idsObjects) {
        List<ObjetosEntity> objetos = objectsRepository.getObjectsByIdsObjects(idsObjects);

        return objetos.stream()
                .map(objeto -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    objeto.getIdObjeto(), "objetos3d");

                    return imageOpt.map(image -> convertToObjectDto(objeto, image));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteObject(Long idObject) {
        Optional<AlmacenamientoEntity> objectOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idObject, "3d");

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idObject, "objetos");

        Optional<ObjetosEntity> objetoOpt = objectsRepository.getByIdObject(idObject);

        // Validar existencia
        if (objetoOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el objeto con ID: " + idObject);
        }

        // Eliminar archivos si existen
        objectOpt.ifPresent(object -> {
            try {
                storageService.deleteFileById(object.getIdArchivo());
            } catch (IOException e) {
                // log error si usas loggers
                System.err.println("Error al eliminar archivo del objeto: " + e.getMessage());
            }
        });

        imageOpt.ifPresent(image -> {
            try {
                storageService.deleteFileById(image.getIdArchivo());
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen asociada al objeto: " + e.getMessage());
            }
        });

        objetoOpt.ifPresent(object -> objectsRepository.deleteObject(object.getIdObjeto()));
    }

    private Optional<ObjectCompleteDto> getObjectDto(Long idObject, String entity) {
        Optional<ObjetosEntity> objetoOpt = objectsRepository.getByIdObject(idObject);

        if (objetoOpt.isEmpty()) {
            return Optional.empty();
        }

        ObjetosEntity object = objetoOpt.get();

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(object.getIdObjeto(), entity);

        if (imageOpt.isEmpty()) {
            return Optional.empty();
        }

        ObjectCompleteDto dto = convertToObjectDto(object, imageOpt.get());
        return Optional.of(dto);
    }


    private ObjectCompleteDto convertToObjectDto(ObjetosEntity object, AlmacenamientoEntity imagen) {

        ObjectCompleteDto objectDto = new ObjectCompleteDto();
        objectDto.setObjectId(object.getIdObjeto());
        objectDto.setObjectName(object.getNombreObjeto());
        objectDto.setHeight(object.getAlto());
        objectDto.setWidth(object.getAncho());
        objectDto.setDepth(object.getProfundidad());
        objectDto.setStatus(object.isEstado());
        objectDto.setObject(convertToStorageDto(imagen));

        return objectDto;
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


