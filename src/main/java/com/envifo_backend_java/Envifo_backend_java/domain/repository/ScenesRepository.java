package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.EscenariosEntity;

import java.util.List;
import java.util.Optional;

public interface ScenesRepository {
    Optional<EscenariosEntity> getByIdScene(Long idScene);
    List<EscenariosEntity> getSceneByNameAndSectionCategory(String nameCategory, String section);
    EscenariosEntity saveScene (EscenariosEntity scene);
    void deleteScene (Long idScene);
}
