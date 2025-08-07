package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.MaterialCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.MaterialDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MaterialsService {
    String saveMaterial(MaterialDto materialDto, MultipartFile material3d, MultipartFile imagen) throws IOException;

    String updateMaterial(Long idMaterial, MaterialDto materialDto, MultipartFile material3d, MultipartFile imagen) throws IOException;

    Optional<MaterialCompleteDto> getByIdMaterial(Long idMaterial);

    //3d
    Optional<MaterialCompleteDto> getMaterial3dByIdMaterial(Long idMaterial);

    List<MaterialCompleteDto> getByNameAndSectionCategory(String nameCategory, Long idCliente);

    //3d
    List<MaterialCompleteDto> getMaterialsByIds(List<Long> ids);

    List<MaterialCompleteDto> getByClienteId(Long idCliente);

    List<MaterialCompleteDto> getGlobalMaterials();

    void deleteMaterial(Long idMaterial);
}
