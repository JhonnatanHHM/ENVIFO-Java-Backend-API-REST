package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ScenesService {

    String saveScene(SceneDto sceneDto, MultipartFile scene,MultipartFile imagen) throws IOException;

    Optional<SceneCompleteDto> getScenedByIdScene(Long idScene);

    Optional<SceneCompleteDto> getSceneImgByIdTexture(Long idScene);

    List<SceneCompleteDto> getByNameAndSectionCategory(String nameCategory, String section);
    void deleteScene(Long idScene) throws IOException;
}
