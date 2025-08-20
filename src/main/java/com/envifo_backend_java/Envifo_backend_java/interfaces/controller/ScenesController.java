package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.SceneDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.ScenesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/scenes")
@Tag(name = "Escenas", description = "Operaciones para gestión de escenas 3D")
public class ScenesController {

    private ObjectMapper objectMapper;
    private final ScenesService scenesService;

    @Autowired
    public ScenesController(ScenesService scenesService, ObjectMapper objectMapper) {
        this.scenesService = scenesService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Guardar una nueva escena con archivos", description = "Guarda una escena y sube archivos de escena e imagen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escena guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o en la carga de archivos")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveScene(
            @RequestPart("scene") @Parameter(description = "DTO con los datos de la escena", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SceneDto.class)))
            String sceneJson,
            @RequestPart(value = "archivoEscena", required = false) MultipartFile archivoEscena,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            // Convertir JSON a DTO
            SceneDto sceneDto = objectMapper.readValue(sceneJson, SceneDto.class);
            String result = scenesService.saveScene(sceneDto, archivoEscena, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar la escena: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener escena completa (archivo .3D) por ID", description = "Devuelve una escena completa con su archivo asociado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escena encontrada",
                    content = @Content(schema = @Schema(implementation = SceneCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Escena no encontrada")
    })
    @GetMapping("/{idScene}")
    public ResponseEntity<SceneCompleteDto> getSceneById(
            @Parameter(description = "ID de la escena", required = true)
            @PathVariable Long idScene) {
        Optional<SceneCompleteDto> result = scenesService.getScenedByIdScene(idScene);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener imagen de escena por ID", description = "Devuelve solo la imagen asociada a una escena.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen encontrada",
                    content = @Content(schema = @Schema(implementation = SceneCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/image/{idScene}")
    public ResponseEntity<SceneCompleteDto> getSceneImageById(
            @Parameter(description = "ID de la escena", required = true)
            @PathVariable Long idScene) {
        Optional<SceneCompleteDto> result = scenesService.getSceneImgByIdTexture(idScene);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar escena por ID", description = "Elimina una escena y sus archivos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Escena eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Escena no encontrada")
    })
    @DeleteMapping("/{idScene}")
    public ResponseEntity<Void> deleteScene(@PathVariable Long idScene) {
        try {
            scenesService.deleteScene(idScene);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener escenas por nombre de categoría", description = "Lista todas las escenas según nombre de categoría.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escenas encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SceneCompleteDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron escenas")
    })
    @GetMapping("/category/{name}")
    public ResponseEntity<List<SceneCompleteDto>> getByCategoryName(
            @Parameter(description = "Nombre de la categoría") @PathVariable String name) {
        List<SceneCompleteDto> list = scenesService.getByNameAndSectionCategory(name, "escenarios");
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }
}
