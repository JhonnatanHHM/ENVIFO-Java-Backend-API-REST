package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.Designs3dRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.Designs3dCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class Disenios3dRepository implements Designs3dRepository {

    private Designs3dCrudRepository designs3dCrudRepository;

    @Autowired
    public Disenios3dRepository(Designs3dCrudRepository designs3dCrudRepository) {
        this.designs3dCrudRepository = designs3dCrudRepository;
    }

    @Override
    public Optional<Disenios3dEntity> getDesignById(Long idDesign) {
        return designs3dCrudRepository.findById(idDesign);
    }

    @Override
    public Disenios3dEntity saveDesign(Disenios3dEntity design) {
        return designs3dCrudRepository.saveAndFlush(design);
    }

    @Override
    public void deleteDesign(Long idDesign) {
        designs3dCrudRepository.deleteById(idDesign);
    }
}
