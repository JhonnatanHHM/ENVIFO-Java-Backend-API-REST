package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectFilteredDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.ProjectsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/projects")
@Tag(name = "Proyectos", description = "Operaciones para la gestión de proyectos")
public class ProjectsController {

    private final ProjectsService projectsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProjectsController(ProjectsService projectsService, ObjectMapper objectMapper) {
        this.projectsService = projectsService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Guardar un nuevo proyecto", description = "Guarda un proyecto con su diseño 3D y una imagen asociada (opcional)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proyecto guardado correctamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o en el procesamiento"),
            @ApiResponse(responseCode = "404", description = "Usuario o cliente no encontrado")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveProject(
            @RequestPart("project") @Parameter(description = "DTO con los datos del proyecto en formato JSON", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProjectDto.class)))
            String projectJson,
            @RequestPart(value = "image", required = false) @Parameter(description = "Imagen del proyecto (opcional)", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile image) {
        try {
            if (projectJson == null || projectJson.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'project' es requerido y no puede estar vacío");
            }

            // Convertir JSON a DTO
            ProjectDto projectDto = objectMapper.readValue(projectJson, ProjectDto.class);
            if (projectDto.getDesign3d() == null) {
                return ResponseEntity.badRequest().body("El campo 'design3d' es requerido en el ProjectDto");
            }

            String result = projectsService.saveProject(projectDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error al deserializar el JSON del proyecto: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar un proyecto existente", description = "Actualiza los datos de un proyecto y su imagen asociada (opcional)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados o en el procesamiento"),
            @ApiResponse(responseCode = "404", description = "Proyecto, usuario o cliente no encontrado")
    })
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProject(
            @RequestPart("project") @Parameter(description = "DTO con los datos del proyecto en formato JSON", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProjectDto.class)))
            String projectJson,
            @RequestPart(value = "image", required = false) @Parameter(description = "Imagen del proyecto (opcional)", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile image) {
        try {
            if (projectJson == null || projectJson.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'project' es requerido y no puede estar vacío");
            }

            // Convertir JSON a DTO
            ProjectDto projectDto = objectMapper.readValue(projectJson, ProjectDto.class);
            if (projectDto.getIdProject() == null) {
                return ResponseEntity.badRequest().body("El campo 'idProject' es requerido para actualizar el proyecto");
            }

            String result = projectsService.updateProject(projectDto, image);
            return ResponseEntity.ok(result);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error al deserializar el JSON del proyecto: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener proyecto por ID", description = "Devuelve los detalles completos de un proyecto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto encontrado",
                    content = @Content(schema = @Schema(implementation = ProjectCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{idProject}")
    public ResponseEntity<ProjectCompleteDto> getByIdProject(@PathVariable Long idProject) throws JsonProcessingException {
        Optional<ProjectCompleteDto> projectOpt = projectsService.getByIdProject(idProject);
        return projectOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar proyecto", description = "Elimina un proyecto por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proyecto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @DeleteMapping("/{idProject}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long idProject) {
        try {
            projectsService.deleteProject(idProject);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener proyectos por cliente", description = "Lista todos los proyectos asociados a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyectos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectFilteredDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron proyectos")
    })
    @GetMapping("/customer/{idCustomer}")
    public ResponseEntity<List<ProjectFilteredDto>> getByCustomer(@PathVariable Long idCustomer) {
        List<ProjectFilteredDto> list = projectsService.getProjectByCustomer(idCustomer);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener proyectos por usuario", description = "Lista todos los proyectos creados por un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyectos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectFilteredDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron proyectos")
    })
    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<ProjectFilteredDto>> getByUser(@PathVariable Long idUser) {
        List<ProjectFilteredDto> list = projectsService.getProjectByUser(idUser);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @Operation(summary = "Obtener proyectos por usuario y cliente", description = "Lista todos los proyectos de un cliente creados por un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyectos encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectFilteredDto.class)))),
            @ApiResponse(responseCode = "204", description = "No se encontraron proyectos")
    })
    @GetMapping("/user/{idUser}/customer/{idCustomer}")
    public ResponseEntity<List<ProjectFilteredDto>> getByUserAndCustomer(
            @PathVariable Long idUser,
            @PathVariable Long idCustomer) {
        List<ProjectFilteredDto> list = projectsService.getProjectByUserAndCustomer(idUser, idCustomer);
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }
}