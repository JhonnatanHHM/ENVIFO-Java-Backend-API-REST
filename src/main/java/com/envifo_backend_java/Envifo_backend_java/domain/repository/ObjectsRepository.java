package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ObjetosEntity;


import java.util.List;
import java.util.Optional;

public interface ObjectsRepository {
    Optional<ObjetosEntity> getByIdObject(Long idObject);
    List<ObjetosEntity> getObjectByNameAndSectionCategory(String nameCategory, String section);
    List<ObjetosEntity> getObjectsByIdsObjects(List<Long> idsObjects);
    ObjetosEntity saveObject (ObjetosEntity object);
    void deleteObject (Long idObject);
}
