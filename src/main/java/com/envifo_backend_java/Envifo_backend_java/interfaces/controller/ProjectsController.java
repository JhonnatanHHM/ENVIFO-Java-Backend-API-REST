package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.ObjectDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.ProjectFilteredDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.ProjectsService;
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
@RequestMapping("/api/projects")
@Tag(name = "Proyectos", description = "Operaciones para la gestión de proyectos")
public class ProjectsController {

    private final ProjectsService projectsService;
    private ObjectMapper objectMapper;

    @Autowired
    public ProjectsController(ProjectsService projectsService, ObjectMapper objectMapper) {
        this.projectsService = projectsService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Guardar un nuevo proyecto", description = "Guarda un proyecto con su imagen asociada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proyecto guardado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al guardar el proyecto")
    })
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveProject(
            @RequestPart("project") @Parameter(description = "DTO con los datos del proyecto", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProjectDto.class)))
            String projectJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convertir JSON a DTO
            ProjectDto projectDto = objectMapper.readValue(projectJson, ProjectDto.class);
            String result = projectsService.saveProject(projectDto, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al guardar el proyecto: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar proyecto", description = "Actualiza un proyecto con nuevos datos e imagen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al actualizar el proyecto")
    })
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProject(
            @RequestPart("project") @Parameter(description = "DTO con los datos del proyecto", required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProjectDto.class)))
            String projectJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            // Convertir JSON a DTO
            ProjectDto projectDto = objectMapper.readValue(projectJson, ProjectDto.class);
            String result = projectsService.updateProject(projectDto, image);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al actualizar el proyecto: " + e.getMessage());
        }
    }

    @Operation(summary = "Obtener proyecto por ID", description = "Devuelve un proyecto completo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto encontrado",
                    content = @Content(schema = @Schema(implementation = ProjectCompleteDto.class))),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{idProject}")
    public ResponseEntity<ProjectCompleteDto> getByIdProject(@PathVariable Long idProject) {
        Optional<ProjectCompleteDto> projectOpt = projectsService.getByIdProject(idProject);
        return projectOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
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
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
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
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
