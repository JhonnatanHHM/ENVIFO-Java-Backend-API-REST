package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.GradeServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.dto.GradesDto;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Notas", description = "Operaciones relacionadas con la gestión de notas de usuarios")
public class GradeController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private GradeServiceImple gradeServiceImple;

    @Operation(summary = "Obtener nota por ID",
            description = "Devuelve una nota según su ID",
            parameters = {
                    @Parameter(name = "idGrade", description = "ID de la nota", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Nota encontrada"),
                    @ApiResponse(responseCode = "404", description = "Nota no encontrada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido o no proporcionado")
            }
    )
    @GetMapping("/{idGrade}")
    public ResponseEntity<?> getIdGrade(
                                        @PathVariable Long idGrade) {

        Optional<GradesDto> grade = gradeServiceImple.getByIdGrade(idGrade);

        if (grade.isPresent()) {
            return ResponseEntity.ok(grade.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Nota no encontrada"));
        }
    }

    @Operation(summary = "Listar todas las notas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de notas devuelto exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Token inválido o no proporcionado")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<?> getAllGrades() {

        List<GradesDto> grades = gradeServiceImple.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Obtener notas por usuario")
    @GetMapping("/user/{idUsuario}")
    public ResponseEntity<?> getGradesByUser(
                                             @PathVariable Long idUsuario) {

        List<GradesDto> grades = gradeServiceImple.getByIdUsuario(idUsuario);
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Obtener notas por cliente")
    @GetMapping("/customer/{idCliente}")
    public ResponseEntity<?> getGradesByCustomer(
            @PathVariable Long idCliente) {
        List<GradesDto> grades = gradeServiceImple.getByIdCustomer(idCliente);
        return ResponseEntity.ok(grades);
    }


    @Operation(summary = "Buscar notas por título o contenido")
    @GetMapping("/filter/{data}")
    public ResponseEntity<?> getByTitleOrContent(
                                                 @PathVariable String data) {

        List<GradesDto> grades = gradeServiceImple.searchByTitleOrContent(data);
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Buscar notas por título/contenido filtradas por usuario")
    @GetMapping("/filter/user/{data}/{idUsuario}")
    public ResponseEntity<?> getGradesFilterByUser(
                                                   @PathVariable String data,
                                                   @PathVariable Long idUsuario) {

        List<GradesDto> grades = gradeServiceImple.getGradesFilterByUser(data, idUsuario);
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Buscar notas por título/contenido filtradas por cliente")
    @GetMapping("/filter/client/{data}/{idCliente}")
    public ResponseEntity<?> getGradesFilterByClient(
                                                     @PathVariable String data,
                                                     @PathVariable Long idCliente) {

        List<GradesDto> grades = gradeServiceImple.getGradesFilterByClient(data, idCliente);
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Editar nota")
    @PutMapping("/editGrade")
    public ResponseEntity<?> editGrade(
                                       @RequestBody GradesDto gradeDto) {

        try {
            gradeServiceImple.editGrade(gradeDto);
            return ResponseEntity.ok("Nota actualizada con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    @Operation(summary = "Guardar nueva nota")
    @PostMapping("/save")
    public ResponseEntity<?> saveGrade(
                                       @RequestBody GradesDto grade) {

        GradesDto savedGrade = gradeServiceImple.save(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    @Operation(summary = "Eliminar nota por ID")
    @DeleteMapping("/{idGrade}")
    public ResponseEntity<?> deleteGrade(
                                         @PathVariable Long idGrade) {
        gradeServiceImple.delete(idGrade);
        return ResponseEntity.ok("Nota eliminada con éxito");
    }

}
