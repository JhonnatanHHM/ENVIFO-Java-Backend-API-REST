package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;

import java.util.List;
import java.util.Optional;


public interface CategoriesRepository {
    CategoriasEntity saveCategory(CategoriasEntity category);
    List<CategoriasEntity> getAllBySeccionIn(String seccion);
    List<CategoriasEntity> getCategoriesByIdCliente(Long idCustomer);
    List<CategoriasEntity> getCategoriesGlobals();
    Optional<CategoriasEntity> getCategoryById(Long idCategory);
    void deleteByIdCategory (Long idCategory);
}
