package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ObjetosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ObjectsRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.ObjectsCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ObjetosRepository implements ObjectsRepository {

    private ObjectsCrudRepository objectsCrudRepository;

    @Autowired
    public ObjetosRepository(ObjectsCrudRepository objectsCrudRepository) {
        this.objectsCrudRepository = objectsCrudRepository;
    }

    @Override
    public Optional<ObjetosEntity> getByIdObject(Long idObject) {
        return objectsCrudRepository.findById(idObject);
    }

    @Override
    public List<ObjetosEntity> getObjectByNameAndSectionCategory(String nameCategory, String section) {
        return objectsCrudRepository.findAllByCategoriaNombreAndCategoriaSeccion(nameCategory,section);
    }

    @Override
    public List<ObjetosEntity> getObjectsByIdsObjects(List<Long> idsObjects) {
        return objectsCrudRepository.findAllById(idsObjects);
    }

    @Override
    public ObjetosEntity saveObject(ObjetosEntity object) {
        return objectsCrudRepository.saveAndFlush(object);
    }

    @Override
    public void deleteObject(Long idObject) {
        objectsCrudRepository.deleteById(idObject);
    }
}
