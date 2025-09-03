package com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.crud.CategoriesCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public class CategoriasRepository implements CategoriesRepository {

    private CategoriesCrudRepository categoriesCrudRepository;

    @Autowired
    public CategoriasRepository(CategoriesCrudRepository categoriesCrudRepository) {
        this.categoriesCrudRepository = categoriesCrudRepository;
    }

    @Override
    public CategoriasEntity saveCategory(CategoriasEntity category) {
        return categoriesCrudRepository.save(category);
    }

    @Override
    public List<CategoriasEntity> getAllBySeccionIn(String seccion) {
        return categoriesCrudRepository.findAllBySeccion(seccion);
    }

    @Override
    public List<CategoriasEntity> getCategoriesByIdCliente(Long idCustomer) {
        return categoriesCrudRepository.findAllByClienteIdCliente(idCustomer);
    }

    @Override
    public List<CategoriasEntity> getCategoriesGlobals() {
        return categoriesCrudRepository.findCategoriesGlobales();
    }

    @Override
    public Optional<CategoriasEntity> getCategoryById(Long idCategory) {
        return categoriesCrudRepository.findById(idCategory);
    }

    @Override
    public void deleteByIdCategory(Long idCategory) {
        categoriesCrudRepository.deleteById(idCategory);
    }
}
