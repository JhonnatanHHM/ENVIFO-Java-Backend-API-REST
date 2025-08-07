package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.TextureCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TexturesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.TexturesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/api/textures")
@Tag(name = "Texturas", description = "Operaciones para gestión de texturas y desplazamientos")
public class TexturesController {

    private final TexturesService texturesService;

    @Autowired
    public TexturesController(TexturesService texturesService) {
        this.texturesService = texturesService;
    }

    @Operation(summary = "Guardar una nueva textura con archivos", description = "Guarda una textura y sube archivos de textura e imagen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Textura guardada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o en la carga de archivos")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveTexture(
            @RequestPart("texture") @Parameter(description = "DTO con los datos de la textura", required = true,
                    content = @Content(schema = @Schema(implementation = TexturesDto.class)))
            TexturesDto textureDto,
            @RequestPart(value = "textura", required = true) MultipartFile textura,
            @RequestPart(value = "imagen", required = true) MultipartFile imagen) {
        try {
            String result = texturesService.saveTexture(textureDto, textura, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar la textura: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener textura completa por ID", description = "Devuelve una textura completa con imagen por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Textura encontrada",
                    content = @Content(schema = @Schema(implementation = TextureCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Textura no encontrada")
    })
    @GetMapping("/{idTexture}")
    public ResponseEntity<TextureCompleteDto> getTextureById(
            @Parameter(in = ParameterIn.PATH, description = "ID de la textura", required = true)
            @PathVariable Long idTexture) {
        Optional<TextureCompleteDto> textureOpt = texturesService.getTextureByIdTexture(idTexture);
        return textureOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener desplazamiento por ID de textura", description = "Devuelve solo el displacement asociado a una textura.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desplazamiento encontrado",
                    content = @Content(schema = @Schema(implementation = TextureCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Desplazamiento no encontrado")
    })
    @GetMapping("/displacement/{idTexture}")
    public ResponseEntity<TextureCompleteDto> getDisplacementByIdTexture(
            @PathVariable Long idTexture) {
        Optional<TextureCompleteDto> textureOpt = texturesService.getDisplacementByIdTexture(idTexture);
        return textureOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar textura por ID", description = "Elimina una textura y sus archivos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Textura eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Textura no encontrada")
    })
    @DeleteMapping("/{idTexture}")
    public ResponseEntity<Void> deleteTexture(@PathVariable Long idTexture) {
        try {
            texturesService.deleteTexture(idTexture);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener texturas por nombre de categoría", description = "Lista todas las texturas según nombre de categoría.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de texturas encontradas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TextureCompleteDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron texturas")
    })
    @GetMapping("/category/{name}")
    public ResponseEntity<List<TextureCompleteDto>> getByCategoryName(
            @Parameter(description = "Nombre de la categoría") @PathVariable String name) {
        List<TextureCompleteDto> list = texturesService.getByNameAndSectionCategory(name, "texturas");
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
