package com.envifo_backend_java.Envifo_backend_java.domain.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;

import java.util.List;
import java.util.Optional;


public interface CategoriesRepository {
    CategoriasEntity saveCategory(CategoriasEntity category);
    List<CategoriasEntity> getCategoriesByName(List<String> names);
    List<CategoriasEntity> getCategoriesByIdCliente(Long idCustomer);
    Optional<CategoriasEntity> getCategoryById(Long idCategory);
    void deleteByIdCategory (Long idCategory);
}
