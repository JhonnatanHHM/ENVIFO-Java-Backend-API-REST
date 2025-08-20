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
