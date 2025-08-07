package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CategoriesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.CategoriasEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.service.CategoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categorías", description = "Operaciones relacionadas con las categorías de la APP")
public class CategoriesController {

    private final CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @Operation(summary = "Crear una nueva categoría", description = "Registra una nueva categoría en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriasEntity.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping("/save")
    public ResponseEntity<CategoriesDto> createCategory(
            @RequestBody CategoriesDto dto) {
        try {
            CategoriesDto savedCategory = categoriesService.saveCategory(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una categoría", description = "Modifica los datos de una categoría existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriasEntity.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PutMapping("/update")
    public ResponseEntity<CategoriesDto> updateCategory(@RequestBody CategoriesDto dto) {
        try {
            CategoriesDto updated = categoriesService.updateCategory(dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener una categoría por ID", description = "Consulta los datos de una categoría usando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesDto.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @GetMapping("/{idCategory}")
    public ResponseEntity<CategoriesDto> getCategoryById(
            @Parameter(description = "ID de la categoría") @PathVariable Long idCategory) {
        Optional<CategoriesDto> category = categoriesService.getCategoryById(idCategory);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Obtener categorías por nombre fijo", description = "Devuelve categorías con nombres predeterminados como Piedra, Mármol, Madera.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías encontradas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesDto.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron categorías", content = @Content)
    })
    @GetMapping("/names")
    public ResponseEntity<List<CategoriesDto>> getCategoriesByNames() {
        List<CategoriesDto> categories = categoriesService.getCategoriesByName();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Obtener categorías por ID de cliente", description = "Lista las categorías asociadas a un cliente específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías encontradas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriesDto.class))),
            @ApiResponse(responseCode = "204", description = "No se encontraron categorías para el cliente", content = @Content)
    })
    @GetMapping("/customer/{idCustomer}")
    public ResponseEntity<List<CategoriesDto>> getCategoriesByIdCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long idCustomer) {
        List<CategoriesDto> categories = categoriesService.getCategoriesByIdCliente(idCustomer);
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @DeleteMapping("/{idCustomer}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID de la categoría a eliminar") @PathVariable Long idCustomer) {
        try {
            categoriesService.deleteByIdCategory(idCustomer);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
