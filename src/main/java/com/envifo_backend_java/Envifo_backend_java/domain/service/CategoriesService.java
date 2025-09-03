package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CategoriesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;

import java.util.List;
import java.util.Optional;

public interface CategoriesService {
    CategoriesDto saveCategory(CategoriesDto dto);

    CategoriesDto updateCategory(CategoriesDto dto);

    Optional<CategoriesDto> getCategoryById(Long idCategoria);

    List<CategoriesDto> getCategoriesBySection(String section) ;

    List<CategoriesDto> getCategoriesByIdCliente(Long idCliente);

    List<CategoriesDto> getCategoriesGlobals();

    void deleteByIdCategory(Long idCategoria);
}
