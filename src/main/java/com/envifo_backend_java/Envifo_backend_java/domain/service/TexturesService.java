package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.TextureCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TexturesDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TexturesService {
    Optional<TextureCompleteDto> getDisplacementByIdTexture(Long idTexture);
    Optional<TextureCompleteDto> getTextureByIdTexture(Long idTexture);
    List<TextureCompleteDto> getByNameAndSectionCategory(String nameCategory, String section);
    String saveTexture (TexturesDto texture, MultipartFile textura, MultipartFile imagen) throws IOException;
    void deleteTexture (Long idTexture) throws IOException;
}
