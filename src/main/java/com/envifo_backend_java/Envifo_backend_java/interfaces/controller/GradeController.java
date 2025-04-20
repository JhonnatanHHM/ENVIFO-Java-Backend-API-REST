package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.GradeServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.dto.GradesDto;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private GradeServiceImple gradeServiceImple;

    @GetMapping("/{idGrade}")
    public ResponseEntity<?> getIdGrade(@RequestHeader(value = "Authorization") String token,
                                      @PathVariable Long idGrade) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token inválido o no proporcionado"));
        }

        Optional<GradesDto> grade = gradeServiceImple.getByIdGrade(idGrade);

        if (grade.isPresent()) {
            return ResponseEntity.ok(grade.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Nota no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGrades(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        List<GradesDto> grades = gradeServiceImple.getAllGrades();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/user/{idUsuario}")
    public ResponseEntity<?> getGradesByUser(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable Long idUsuario) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        List<GradesDto> grades = gradeServiceImple.getByIdUsuario(idUsuario);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/filter/{data}")
    public ResponseEntity<?> getByTitleOrContent(@RequestHeader(value = "Authorization") String token,
                                               @PathVariable String data){
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        List<GradesDto> grades = gradeServiceImple.searchByTitleOrContent(data);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/filter/user/{data}/{idUsuario}")
    public ResponseEntity<?> getGradesFilterByUser (@RequestHeader(value = "Authorization") String token,
                                                    @PathVariable String data,
                                                    @PathVariable Long idUsuario){
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        List<GradesDto> grades = gradeServiceImple.getGradesFilterByUser(data,idUsuario);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/filter/client/{data}/{idCliente}")
    public ResponseEntity<?> getGradesFilterByClient (@RequestHeader(value = "Authorization") String token,
                                                    @PathVariable String data,
                                                    @PathVariable Long idCliente){
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        List<GradesDto> grades = gradeServiceImple.getGradesFilterByClient(data,idCliente);
        return ResponseEntity.ok(grades);
    }

    @PutMapping("/editGrade")
    public ResponseEntity<?> editGrade(@RequestHeader(value = "Authorization") String token,
                                       @RequestBody GradesDto gradeDto) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }

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

    @PostMapping("/save")
    public ResponseEntity<?> saveGrade(@RequestHeader(value = "Authorization") String token,
                                       @RequestBody GradesDto grade) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        GradesDto savedGrade = gradeServiceImple.save(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGrade);
    }

    @DeleteMapping("/{idGrade}")
    public ResponseEntity<?> deleteGrade(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable Long idGrade) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
        gradeServiceImple.delete(idGrade);
        return ResponseEntity.ok("Nota eliminada con éxito");
    }

    private boolean validarToken(String token) {
        try {
            token = token.replace("Bearer ", "").trim();
            return jwtGenerator.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }
}

