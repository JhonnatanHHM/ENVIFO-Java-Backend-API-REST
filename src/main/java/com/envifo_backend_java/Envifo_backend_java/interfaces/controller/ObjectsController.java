package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.TexturesDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.ObjectsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/api/objects")
@Tag(name = "Objetos", description = "Operaciones para la gestión de objetos 3D")
public class ObjectsController {

    private ObjectMapper objectMapper;
    private final ObjectsService objectsService;

    @Autowired
    public ObjectsController(ObjectsService objectsService, ObjectMapper objectMapper) {
        this.objectsService = objectsService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Guardar un nuevo objeto 3D con archivos", description = "Guarda un objeto 3D y sube sus archivos asociados (modelo 3D e imagen).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Objeto guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o en la carga de archivos")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveObject(
            @RequestPart("object") @Parameter(description = "DTO con los datos del objeto", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ObjectDto.class)))
            String objectJson,
            @RequestPart(value = "objeto3d", required = true) MultipartFile objeto3d,
            @RequestPart(value = "imagen", required = true) MultipartFile imagen) {
        try {
            // Convertir JSON a DTO
            ObjectDto objectDto = objectMapper.readValue(objectJson, ObjectDto.class);
            String result = objectsService.saveObject(objectDto, objeto3d, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar el objeto: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener modelo 3D por ID de objeto", description = "Devuelve el objeto completo con el archivo 3D.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Objeto encontrado",
                    content = @Content(schema = @Schema(implementation = ObjectCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Objeto no encontrado")
    })
    @GetMapping("/model/{idObject}")
    public ResponseEntity<ObjectCompleteDto> getModelByIdObject(
            @Parameter(in = ParameterIn.PATH, description = "ID del objeto", required = true)
            @PathVariable Long idObject) {
        Optional<ObjectCompleteDto> objectOpt = objectsService.getObjetc3dByIdObject(idObject);
        return objectOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener imagen por ID de objeto", description = "Devuelve solo la imagen asociada a un objeto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen encontrada",
                    content = @Content(schema = @Schema(implementation = ObjectCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/image/{idObject}")
    public ResponseEntity<ObjectCompleteDto> getImageByIdObject(
            @PathVariable Long idObject) {
        Optional<ObjectCompleteDto> objectOpt = objectsService.getObjectImgByIdTexture(idObject);
        return objectOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar objeto por ID", description = "Elimina un objeto 3D y sus archivos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Objeto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Objeto no encontrado")
    })
    @DeleteMapping("/{idObject}")
    public ResponseEntity<Void> deleteObject(@PathVariable Long idObject) throws IOException {
        try {
            objectsService.deleteObject(idObject);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener objetos por nombre de categoría", description = "Lista todos los objetos según el nombre de la categoría.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de objetos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ObjectCompleteDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron objetos")
    })
    @GetMapping("/category/{name}")
    public ResponseEntity<List<ObjectCompleteDto>> getByCategoryName(
            @Parameter(description = "Nombre de la categoría") @PathVariable String name) {
        List<ObjectCompleteDto> list = objectsService.getByNameAndSectionCategory(name, "objetos");
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
