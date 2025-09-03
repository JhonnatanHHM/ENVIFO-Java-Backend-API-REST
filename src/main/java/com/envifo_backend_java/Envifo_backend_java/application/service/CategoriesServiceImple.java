package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CategoriesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CategoriesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.domain.service.CategoriesService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriesServiceImple implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    private final ClientesRepository clientesRepository;

    @Autowired
    public CategoriesServiceImple(CategoriesRepository categoriesRepository, ClientesRepository clientesRepository) {
        this.categoriesRepository = categoriesRepository;
        this.clientesRepository = clientesRepository;
    }

    @Override
    public CategoriesDto saveCategory(CategoriesDto dto) {
        CategoriasEntity categoria = new CategoriasEntity();
        categoria.setNombre(dto.getNombre());
        categoria.setSeccion(dto.getSection());
        categoria.setEstado(true);
        categoria.setFechaCreacion(LocalDateTime.now());

        if (dto.getIdCliente() != null) {
            ClientesEntity cliente = clientesRepository.getByIdCliente(dto.getIdCliente())
                    .orElseThrow(() -> new NotFoundException(
                            "Cliente no encontrado! con ID: " + dto.getIdCliente()
                    ));
            categoria.setCliente(cliente);
        } else {
            // Cliente nulo -> categoría global
            categoria.setCliente(null);
        }

        CategoriasEntity category = categoriesRepository.saveCategory(categoria);

        return convertToCategoriesDto(category);
    }


    @Override
    public CategoriesDto updateCategory(CategoriesDto dto) {
        CategoriasEntity existente = categoriesRepository.getCategoryById(dto.getIdCategoria())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + dto.getIdCategoria()));

        existente.setNombre(dto.getNombre());
        existente.setSeccion(dto.getSection());
        existente.setEstado(dto.getEstado());

        // Actualizar cliente si viene un nuevo ID
        if (dto.getIdCliente() != null) {
            ClientesEntity cliente = new ClientesEntity();
            cliente.setIdCliente(dto.getIdCliente());
            existente.setCliente(cliente);
        }

        CategoriasEntity newCategory = categoriesRepository.saveCategory(existente);

        return convertToCategoriesDto(newCategory);
    }

    @Override
    public Optional<CategoriesDto> getCategoryById(Long idCategoria) {
        CategoriasEntity categoria = categoriesRepository.getCategoryById(idCategoria)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));

        CategoriesDto dto = new CategoriesDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setSection(categoria.getSeccion());
        dto.setEstado(categoria.getEstado());
        dto.setIdCliente(categoria.getCliente() != null ? categoria.getCliente().getIdCliente() : null);

        return Optional.of(dto);
    }

    @Override
    public List<CategoriesDto> getCategoriesBySection(String section) {

        return categoriesRepository.getAllBySeccionIn(section).stream()
                .map(c -> new CategoriesDto(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getSeccion(),
                        c.getEstado(),
                        c.getCliente() != null ? c.getCliente().getIdCliente() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriesDto> getCategoriesGlobals() {

        return categoriesRepository.getCategoriesGlobals().stream()
                .map(c -> new CategoriesDto(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getSeccion(),
                        c.getEstado(),
                        c.getCliente() != null ? c.getCliente().getIdCliente() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoriesDto> getCategoriesByIdCliente(Long idCliente) {
        return categoriesRepository.getCategoriesByIdCliente(idCliente).stream()
                .map(c -> new CategoriesDto(
                        c.getIdCategoria(),
                        c.getNombre(),
                        c.getSeccion(),
                        c.getEstado(),
                        c.getCliente() != null ? c.getCliente().getIdCliente() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByIdCategory(Long idCategoria) {
        categoriesRepository.deleteByIdCategory(idCategoria);
    }

    private CategoriesDto convertToCategoriesDto(CategoriasEntity category) {
        CategoriesDto categoriesDto = new CategoriesDto();
        categoriesDto.setIdCategoria(category.getIdCategoria());
        categoriesDto.setNombre(category.getNombre());
        categoriesDto.setSection(category.getSeccion());
        categoriesDto.setEstado(category.getEstado());
        categoriesDto.setIdCliente(
                category.getCliente() != null ? category.getCliente().getIdCliente() : null
        );
        return categoriesDto;
    }

}
