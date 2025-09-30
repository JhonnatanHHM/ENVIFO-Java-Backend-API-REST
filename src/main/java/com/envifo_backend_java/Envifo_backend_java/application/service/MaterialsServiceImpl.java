package com.envifo_backend_java.Envifo_backend_java.application.service;


import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.*;

import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CustomerMaterialRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.MaterialsService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.TexturesService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialsServiceImpl implements MaterialsService {

    private final MaterialsRepository materialsRepository;
    private final CategoriesRepository categoriesRepository;
    private final TexturesService texturesService;
    private final TexturesRepository texturesRepository;
    private final StorageService storageService;
    private final StorageRepository storageRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMaterialRepository customerMaterialRepository;

    @Autowired
    public MaterialsServiceImpl(MaterialsRepository materialsRepository, CategoriesRepository categoriesRepository, TexturesService texturesService, TexturesRepository texturesRepository, StorageService storageService, StorageRepository storageRepository, CustomerRepository customerRepository, CustomerMaterialRepository customerMaterialRepository) {
        this.materialsRepository = materialsRepository;
        this.categoriesRepository = categoriesRepository;
        this.texturesService = texturesService;
        this.texturesRepository = texturesRepository;
        this.storageService = storageService;
        this.storageRepository = storageRepository;
        this.customerRepository = customerRepository;
        this.customerMaterialRepository = customerMaterialRepository;
    }

    @Transactional
    @Override
    public String saveMaterial(MaterialDto materialDto, MultipartFile material3d, MultipartFile imagen) throws IOException {

        // Crear entidad Material
        MaterialesEntity material = new MaterialesEntity();
        material.setNombre(materialDto.getNameMaterial());
        material.setDescripcionMate(materialDto.getDescripcionMate());
        material.setAlto(materialDto.getHeight());
        material.setAncho(materialDto.getWidth());
        material.setEstado(materialDto.isStatus());

        // Asociar categoría
        CategoriasEntity categoria = categoriesRepository.getCategoryById(materialDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        material.setCategoria(categoria);

        // Asociar textura si existe
        if (materialDto.getIdTextura() != null) {
            texturesRepository.getByIdTexture(materialDto.getIdTextura())
                    .ifPresent(material::setTextura);
        }

        // Guardar primero el material
        MaterialesEntity materialSaved = materialsRepository.saveMaterial(material);

        // Asociar cliente si existe
        if (materialDto.getIdCliente() != null) {
            ClientesEntity cliente = customerRepository.getByIdCliente(materialDto.getIdCliente())
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado!"));

            ClienteMaterialEntity newCliMat = new ClienteMaterialEntity();
            newCliMat.setCliente(cliente);
            newCliMat.setMaterial(materialSaved);

            customerMaterialRepository.saveClienteMaterial(newCliMat);
        }

        // Procesar archivo 3D si se envía
        if (material3d != null && !material3d.isEmpty()) {
            StorageDto dto = new StorageDto();
            dto.setIdEntity(materialSaved.getIdMaterial());
            storageService.saveFile(material3d, dto, "materiales3d", "3d");
        }

        // Procesar imagen si se envía
        if (imagen != null && !imagen.isEmpty()) {
            StorageDto dto = new StorageDto();
            dto.setIdEntity(materialSaved.getIdMaterial());
            storageService.saveFile(imagen, dto, "materiales", "imagenes");
        }

        return "Material creado correctamente";
    }



    @Override
    public String updateMaterial(Long idMaterial, MaterialDto materialDto, MultipartFile material3d, MultipartFile imagen) throws IOException {
        // Verificar si el material existe
        MaterialesEntity existingMaterial = materialsRepository.getByIdMaterial(idMaterial)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el material con ID: " + idMaterial));

        // Actualizar atributos
        existingMaterial.setNombre(materialDto.getNameMaterial());
        existingMaterial.setDescripcionMate(materialDto.getDescripcionMate());
        existingMaterial.setAlto(materialDto.getHeight());
        existingMaterial.setAncho(materialDto.getWidth());
        existingMaterial.setEstado(materialDto.isStatus());

        // Actualizar categoría
        CategoriasEntity categoria = categoriesRepository.getCategoryById(materialDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        existingMaterial.setCategoria(categoria);

        // Actualizar textura si se proporciona
        if (materialDto.getIdTextura() != null) {
            texturesRepository.getByIdTexture(materialDto.getIdTextura())
                    .ifPresent(existingMaterial::setTextura);
        }

        // Guardar cambios del material
        MaterialesEntity updatedMaterial = materialsRepository.saveMaterial(existingMaterial);

        // Asociar cliente si existe (igual que en saveMaterial)
        if (materialDto.getIdCliente() != null) {
            ClientesEntity cliente = customerRepository.getByIdCliente(materialDto.getIdCliente())
                    .orElseThrow(() -> new NotFoundException("Cliente no encontrado!"));

            // Si ya existe relación, opcionalmente eliminarla o actualizarla
            customerMaterialRepository.deleteByMaterialId(updatedMaterial.getIdMaterial());

            ClienteMaterialEntity newCliMat = new ClienteMaterialEntity();
            newCliMat.setCliente(cliente);
            newCliMat.setMaterial(updatedMaterial);

            customerMaterialRepository.saveClienteMaterial(newCliMat);
        }

        // Reemplazar archivo 3D si se proporciona
        if (material3d != null && !material3d.isEmpty()) {
            storageRepository.findByIdEntidadAndTipoEntidad(idMaterial, "materiales3d")
                    .ifPresent(file -> {
                        try {
                            storageService.deleteFileById(file.getIdArchivo());
                        } catch (IOException e) {
                            System.err.println("Error al eliminar archivo 3D anterior: " + e.getMessage());
                        }
                    });

            StorageDto dto = new StorageDto();
            dto.setIdEntity(updatedMaterial.getIdMaterial());
            storageService.saveFile(material3d, dto, "materiales3d", "3d");
        }

        // Reemplazar imagen si se proporciona
        if (imagen != null && !imagen.isEmpty()) {
            storageRepository.findByIdEntidadAndTipoEntidad(idMaterial, "materiales")
                    .ifPresent(image -> {
                        try {
                            storageService.deleteFileById(image.getIdArchivo());
                        } catch (IOException e) {
                            System.err.println("Error al eliminar imagen anterior: " + e.getMessage());
                        }
                    });

            StorageDto dto = new StorageDto();
            dto.setIdEntity(updatedMaterial.getIdMaterial());
            storageService.saveFile(imagen, dto, "materiales", "imagenes");
        }

        return "Material actualizado correctamente";
    }

    @Override
    public Optional<MaterialCompleteDto> getByIdMaterial(Long idMaterial) {

        Optional<MaterialesEntity> matrialOpt = materialsRepository.getByIdMaterial(idMaterial);

        if (matrialOpt.isEmpty()) {
            return Optional.empty();
        }

        MaterialesEntity material = matrialOpt.get();

        return getMaterialDto(material, "materiales",false);
    }

    @Override
    public Optional<MaterialCompleteDto> getLastMaterialByCustomer(Long idCustomer) {

        Pageable pageable = PageRequest.of(0, 1);
        MaterialesEntity material = materialsRepository
                .getLastMaterialByCustomer(idCustomer, pageable)
                .stream()
                .findFirst()
                .orElse(null);


        if (material == null) {
            return Optional.empty();
        }

        return getMaterialDto(material, "materiales",false);
    }

    //3d
    @Override
    public Optional<MaterialCompleteDto> getMaterial3dByIdMaterial(Long idMaterial) {

        Optional<MaterialesEntity> matrialOpt = materialsRepository.getByIdMaterial(idMaterial);

        if (matrialOpt.isEmpty()) {
            return Optional.empty();
        }

        MaterialesEntity material = matrialOpt.get();

        return getMaterialDto(material, "materiales3d",true);
    }

    @Override
    public List<MaterialCompleteDto> getByNameAndSectionCategory(String nameCategory, Long idCliente) {
        List<MaterialesEntity> materiales = materialsRepository.getMaterialByNameAndSectionCategory(nameCategory, idCliente);

        return materiales.stream()
                .map(material -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    material.getIdMaterial(), "materiales");

                    return imageOpt.map(image -> convertToMaterialDto(material, image, false));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    //3d
    @Override
    public List<MaterialCompleteDto> getMaterialsByIds(List<Long> ids) {
        List<MaterialesEntity> materiales = materialsRepository.getObjectsByIdsMaterials(ids);

        return materiales.stream()
                .map(material -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    material.getIdMaterial(), "materiales3d");

                    return imageOpt.map(image -> convertToMaterialDto(material, image, true));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialCompleteDto> getByClienteId(Long idCliente) {

        List<MaterialesEntity> materiales =
                customerMaterialRepository.getMaterialByCliente(idCliente);

        return materiales.stream()
                .map(material -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    material.getIdMaterial(), "materiales");

                    return imageOpt.map(image -> convertToMaterialDto(material, image, false));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialCompleteDto> getGlobalMaterials() {

        List<MaterialesEntity> materiales =
                materialsRepository.findMaterialsGlobales();

        return materiales.stream()
                .map(material -> {
                    Optional<AlmacenamientoEntity> imageOpt =
                            storageRepository.findByIdEntidadAndTipoEntidad(
                                    material.getIdMaterial(), "materiales");

                    return imageOpt.map(image -> convertToMaterialDto(material, image, false));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMaterial(Long idMaterial) {
        Optional<AlmacenamientoEntity> material3dOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idMaterial, "materiales3d");

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(idMaterial, "materiales");

        Optional<MaterialesEntity> materialOpt = materialsRepository.getByIdMaterial(idMaterial);

        // Validar existencia
        if (materialOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el material con ID: " + idMaterial);
        }

        // Eliminar archivos si existen
        material3dOpt.ifPresent(material -> {
            try {
                storageService.deleteFileById(material.getIdArchivo());
            } catch (IOException e) {
                // log error si usas loggers
                System.err.println("Error al eliminar archivo del material: " + e.getMessage());
            }
        });

        imageOpt.ifPresent(image -> {
            try {
                storageService.deleteFileById(image.getIdArchivo());
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen asociada al material: " + e.getMessage());
            }
        });

        materialOpt.ifPresent(material -> materialsRepository.deleteMaterial(material.getIdMaterial()));
    }

    private Optional<MaterialCompleteDto> getMaterialDto(MaterialesEntity material, String entity, boolean texture3d) {

        Optional<AlmacenamientoEntity> imageOpt =
                storageRepository.findByIdEntidadAndTipoEntidad(material.getIdMaterial(), entity);

        if (imageOpt.isEmpty()) {
            return Optional.empty();
        }

        MaterialCompleteDto dto = convertToMaterialDto(material, imageOpt.get(), texture3d);
        return Optional.of(dto);
    }


    private MaterialCompleteDto convertToMaterialDto(MaterialesEntity material, AlmacenamientoEntity imagen, boolean texture3d) {

        MaterialCompleteDto materialDto = new MaterialCompleteDto();
        materialDto.setIdMaterial(material.getIdMaterial());
        materialDto.setNameMaterial(material.getNombre());
        materialDto.setDescripcionMate(material.getDescripcionMate());
        materialDto.setHeight(material.getAlto());
        materialDto.setWidth(material.getAncho());
        materialDto.setStatus(material.isEstado());
        materialDto.setIdCategoria(material.getCategoria().getIdCategoria());
        materialDto.setNameCategory(material.getCategoria().getNombre());

        TextureCompleteDto textureDto = null;

        if (material.getTextura() != null) {
            Optional<TextureCompleteDto> optTexture;
            if (!texture3d) {
                optTexture = texturesService.getTextureByIdTexture(material.getTextura().getIdTextura());
            } else {
                optTexture = texturesService.getDisplacementByIdTexture(material.getTextura().getIdTextura());
            }

            // Asignar si está presente
            if (optTexture.isPresent()) {
                textureDto = optTexture.get();
            }
        }

        // Asignar al DTO (puede ser null si no hay textura)
        materialDto.setTexture(textureDto);
        materialDto.setMaterial(convertToStorageDto(imagen));

        return materialDto;
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
