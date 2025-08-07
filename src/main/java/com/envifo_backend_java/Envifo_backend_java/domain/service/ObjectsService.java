package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ObjectsService {

    String saveObject(ObjectDto objectDto, MultipartFile objeto3d, MultipartFile imagen) throws IOException;

    Optional<ObjectCompleteDto> getObjetc3dByIdObject(Long idObject);

    Optional<ObjectCompleteDto> getObjectImgByIdTexture(Long idObject);

    List<ObjectCompleteDto> getByNameAndSectionCategory(String nameCategory, String section);

    List<ObjectCompleteDto> getObjectsByIdsObjects(List<Long> idsObjects);

    void deleteObject(Long idObject);

}
