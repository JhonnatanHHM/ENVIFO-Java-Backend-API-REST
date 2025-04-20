package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.RolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private RolServiceImple rolServiceImple;

    @GetMapping("/{idRol}")
    public ResponseEntity<?> getByIdRol(@RequestHeader(value = "Authorization") String token,
                                        @PathVariable Long idRol) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token inválido o no proporcionado"));
        }

        Optional<RolDto> rol = rolServiceImple.getByIdRol(idRol);

        if (rol.isPresent()) {
            return ResponseEntity.ok(rol.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Rol no encontrado"));
        }
    }

    // Obtener un rol por nombre "GLOBAL"
    @GetMapping("/rol/GLOBAL")
    public ResponseEntity<?> getRolGlobal(@RequestHeader(value = "Authorization") String token) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido o no proporcionado");
        }

        Optional<RolDto> rol = rolServiceImple.getByname("GLOBAL");
        return rol.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener un rol por nombre "RESTRINGIDO"
    @GetMapping("/rol/RESTRINGIDO")
    public ResponseEntity<?> getRolRestringido(@RequestHeader(value = "Authorization") String token) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token inválido o no proporcionado");
        }

        Optional<RolDto> rol = rolServiceImple.getByname("RESTRINGIDO");
        return rol.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo rol
    @PostMapping("/newRol")
    public ResponseEntity<?> createRol(@RequestHeader(value = "Authorization") String token,
                                       @RequestBody RolDto rolDto) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token inválido o no proporcionado"));
        }

        try {
            RolDto savedRol = rolServiceImple.createRol(rolDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRol);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear rol: " + e.getMessage()));
        }
    }

    // Actualizar un rol existente
    @PutMapping("/editRol")
    public ResponseEntity<?> updateRol(@RequestHeader(value = "Authorization") String token,
                                       @RequestBody RolDto rolDto) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token inválido o no proporcionado"));
        }

        try {
            RolDto updatedRol = rolServiceImple.editRol(rolDto);
            return ResponseEntity.ok(updatedRol);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar rol: " + e.getMessage()));
        }
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
