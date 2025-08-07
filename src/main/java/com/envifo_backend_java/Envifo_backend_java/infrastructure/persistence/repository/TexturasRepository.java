package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.TexturasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.TexturesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.TexturesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TexturasRepository implements TexturesRepository {

    private TexturesCrudRepository texturesCrudRepository;

    @Autowired
    public TexturasRepository(TexturesCrudRepository texturesCrudRepository) {
        this.texturesCrudRepository = texturesCrudRepository;
    }

    @Override
    public Optional<TexturasEntity> getByIdTexture(Long idTextura) {
        return texturesCrudRepository.findById(idTextura);
    }

    @Override
    public List<TexturasEntity> getByNameAndSectionCategory(String nameCategory, String section) {
        return texturesCrudRepository.findAllByCategoriaNombreAndCategoriaSeccion(nameCategory, section);
    }

    @Override
    public TexturasEntity saveTexture(TexturasEntity textura) {
        return texturesCrudRepository.saveAndFlush(textura);
    }

    @Override
    public void deleteTexture(Long idTextura) {
        texturesCrudRepository.deleteById(idTextura);
    }
}
