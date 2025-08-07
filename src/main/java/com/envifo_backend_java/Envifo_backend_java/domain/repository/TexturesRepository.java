package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.TexturasEntity;

import java.util.List;
import java.util.Optional;

public interface TexturesRepository {

    Optional<TexturasEntity> getByIdTexture(Long idTextura);
    List<TexturasEntity> getByNameAndSectionCategory(String nameCategory, String section);
    TexturasEntity saveTexture (TexturasEntity textura);
    void deleteTexture (Long idTextura);

}
