package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.EscenariosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.ScenesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.ScenesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EscenasRepository implements ScenesRepository {

    private ScenesCrudRepository scenesCrudRepository;

    @Autowired
    public EscenasRepository(ScenesCrudRepository scenesCrudRepository) {
        this.scenesCrudRepository = scenesCrudRepository;
    }

    @Override
    public Optional<EscenariosEntity> getByIdScene(Long idScene) {
        return scenesCrudRepository.findById(idScene);
    }

    @Override
    public List<EscenariosEntity> getSceneByNameAndSectionCategory(String nameCategory, String section) {
        return scenesCrudRepository.findAllByCategoriaNombreAndCategoriaSeccion(nameCategory, section);
    }

    @Override
    public EscenariosEntity saveScene(EscenariosEntity scene) {
        return scenesCrudRepository.saveAndFlush(scene);
    }

    @Override
    public void deleteScene(Long idScene) {
        scenesCrudRepository.deleteById(idScene);
    }
}
