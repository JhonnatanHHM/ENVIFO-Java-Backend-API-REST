package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.MaterialCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.MaterialDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.MaterialsService;
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
@RequestMapping("/api/materials")
@Tag(name = "Materiales", description = "Operaciones para la gestión de materiales")
public class MaterialsController {

    private final MaterialsService materialsService;

    @Autowired
    public MaterialsController(MaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    @Operation(summary = "Guardar un nuevo material", description = "Guarda un material con sus archivos asociados (modelo 3D e imagen)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Material guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o en la carga de archivos")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveMaterial(
            @RequestPart("material") @Parameter(description = "DTO con los datos del material", required = true,
                    content = @Content(schema = @Schema(implementation = MaterialDto.class)))
            MaterialDto materialDto,
            @RequestPart(value = "material3d", required = false) MultipartFile material3d,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            String result = materialsService.saveMaterial(materialDto, material3d, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar el material: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar material", description = "Actualiza un material por ID con nuevos datos y archivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error en la actualización")
    })
    @PutMapping(value = "/update/{idMaterial}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateMaterial(
            @PathVariable Long idMaterial,
            @RequestPart("material") MaterialDto materialDto,
            @RequestPart(value = "material3d", required = false) MultipartFile material3d,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            String result = materialsService.updateMaterial(idMaterial, materialDto, material3d, imagen);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar el material: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener imagen del material", description = "Devuelve la imagen del material por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material encontrado",
                    content = @Content(schema = @Schema(implementation = MaterialCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    @GetMapping("/image/{idMaterial}")
    public ResponseEntity<MaterialCompleteDto> getImageByIdMaterial(@PathVariable Long idMaterial) {
        Optional<MaterialCompleteDto> materialOpt = materialsService.getByIdMaterial(idMaterial);
        return materialOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar material", description = "Elimina un material y sus archivos asociados por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    @DeleteMapping("/{idMaterial}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long idMaterial) throws IOException {
        try {
            materialsService.deleteMaterial(idMaterial);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener materiales por categoría", description = "Lista los materiales según el nombre de la categoría y el ID del cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materiales encontrada",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MaterialCompleteDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron materiales")
    })
    @GetMapping("/category/{nameCategory}/client/{idCliente}")
    public ResponseEntity<List<MaterialCompleteDto>> getByCategoryNameAndClient(
            @PathVariable String nameCategory,
            @PathVariable Long idCliente) {
        List<MaterialCompleteDto> list = materialsService.getByNameAndSectionCategory(nameCategory, idCliente);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener materiales de un cliente", description = "Lista todos los materiales asociados a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materiales del cliente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MaterialCompleteDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron materiales")
    })
    @GetMapping("/client/{idCliente}")
    public ResponseEntity<List<MaterialCompleteDto>> getByClientId(@PathVariable Long idCliente) {
        List<MaterialCompleteDto> list = materialsService.getByClienteId(idCliente);
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener materiales globales", description = "Lista todos los materiales que no están asignados a clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materiales globales",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = MaterialCompleteDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron materiales")
    })
    @GetMapping("/global")
    public ResponseEntity<List<MaterialCompleteDto>> getGlobalMaterials() {
        List<MaterialCompleteDto> list = materialsService.getGlobalMaterials();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener modelo 3D por ID de material", description = "Devuelve el material completo con el archivo 3D asociado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Material encontrado",
                    content = @Content(schema = @Schema(implementation = MaterialCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado")
    })
    @GetMapping("/model/{idMaterial}")
    public ResponseEntity<MaterialCompleteDto> getMaterial3dById(
            @Parameter(description = "ID del material", required = true)
            @PathVariable Long idMaterial) {
        Optional<MaterialCompleteDto> materialOpt = materialsService.getMaterial3dByIdMaterial(idMaterial);
        return materialOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
