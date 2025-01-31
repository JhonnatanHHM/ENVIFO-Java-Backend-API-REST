package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.UserServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.UserDom;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserServiceImple userServiceImple;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> getUser(@RequestHeader(value = "Authorization") String token,
                                     @PathVariable Long idUsuario) {

        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }

        Optional<UserDom> userDom = userServiceImple.getByIdUsuario(idUsuario);

        if (userDom.isPresent()) {
            return ResponseEntity.ok(userDom.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }

        List<UserDom> users = userServiceImple.getAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> delete(@RequestHeader(value="Authorization") String token,
                                    @PathVariable Long idUsuario) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
        userServiceImple.delete(idUsuario);
        return ResponseEntity.ok("Usuario eliminado con éxito");
    }

    @PutMapping("/editUser")
    public ResponseEntity<?> editUser(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody UserDom userDom) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o no proporcionado");
        }
        try {
            userServiceImple.editUser(userDom);
            return ResponseEntity.ok("Usuario actualizado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
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

