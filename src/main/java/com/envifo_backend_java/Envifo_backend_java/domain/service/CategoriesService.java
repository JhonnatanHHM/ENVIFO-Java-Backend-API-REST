package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CategoriesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;

import java.util.List;
import java.util.Optional;

public interface CategoriesService {
    CategoriesDto saveCategory(CategoriesDto dto);

    CategoriesDto updateCategory(CategoriesDto dto);

    Optional<CategoriesDto> getCategoryById(Long idCategoria);

    List<CategoriesDto> getCategoriesByName();

    List<CategoriesDto> getCategoriesByIdCliente(Long idCliente);

    void deleteByIdCategory(Long idCategoria);
}
