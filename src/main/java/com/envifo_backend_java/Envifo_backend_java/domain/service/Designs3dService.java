package com.envifo_backend_java.Envifo_backend_java.domain.service;


import com.envifo_backend_java.Envifo_backend_java.application.dto.Designs3dDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.Disenios3dEntity;

import java.util.Optional;

public interface Designs3dService {
    Disenios3dEntity saveDesign(Designs3dDto designDto);
    Disenios3dEntity updateDesign(Designs3dDto dto);
    void deleteDesign(Long id);
}
