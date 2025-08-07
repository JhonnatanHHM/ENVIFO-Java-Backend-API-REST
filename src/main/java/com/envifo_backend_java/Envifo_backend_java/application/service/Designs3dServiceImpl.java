package com.envifo_backend_java.Envifo_backend_java.application.service;


import com.envifo_backend_java.Envifo_backend_java.application.dto.Designs3dDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.Designs3dRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.Designs3dService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Designs3dServiceImpl implements Designs3dService {

    private final Designs3dRepository repository;

    @Autowired
    public Designs3dServiceImpl(Designs3dRepository repository) {
        this.repository = repository;
    }

    @Override
    public Disenios3dEntity saveDesign(Designs3dDto dto) {
        Disenios3dEntity entity = new Disenios3dEntity();
        entity.setConfiguracion(dto.getConfiguracion());
        entity.setMateriales(dto.getMateriales());
        entity.setObjetos(dto.getObjetos());

        Disenios3dEntity saved = repository.saveDesign(entity);
        return saved;
    }

    @Override
    public Disenios3dEntity updateDesign(Designs3dDto dto) {
        Disenios3dEntity entity = new Disenios3dEntity();
        entity.setIdDisenio(dto.getIdDisenio());
        entity.setConfiguracion(dto.getConfiguracion());
        entity.setMateriales(dto.getMateriales());
        entity.setObjetos(dto.getObjetos());

        Disenios3dEntity saved = repository.saveDesign(entity);

        return saved;
    }

    @Override
    public Optional<Designs3dDto> getDesignById(Long id) {
        return repository.getDesignById(id).map(
                entity -> new Designs3dDto(
                        entity.getIdDisenio(),
                        entity.getConfiguracion(),
                        entity.getMateriales(),
                        entity.getObjetos()
                )
        );
    }

    @Override
    public void deleteDesign(Long id) {
        repository.deleteDesign(id);
    }
}
