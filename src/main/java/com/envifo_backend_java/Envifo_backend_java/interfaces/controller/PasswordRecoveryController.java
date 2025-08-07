package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.PasswordRecoveryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recuperacion")
@Tag(name = "Recuperación de Contraseña", description = "Operaciones para recuperar y cambiar contraseña de usuario")
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryServiceImpl recoveryService;

    @Operation(
            summary = "Solicitar token de recuperación",
            description = "Envía un correo electrónico con el token de recuperación al usuario",
            parameters = {
                    @Parameter(name = "email", description = "Correo electrónico del usuario", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Correo enviado con éxito"),
                    @ApiResponse(responseCode = "500", description = "Error al enviar el correo")
            }
    )
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarToken(@RequestParam String email) throws MessagingException {
        recoveryService.iniciarRecuperacion(email);
        return ResponseEntity.ok("Correo enviado");
    }

    @Operation(
            summary = "Validar token de recuperación",
            description = "Verifica si el token de recuperación es válido y no ha expirado",
            parameters = {
                    @Parameter(name = "token", description = "Token de recuperación", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token válido o inválido")
            }
    )
    @GetMapping("/validar")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        boolean valido = recoveryService.validarToken(token);
        return ResponseEntity.ok(valido ? "Token válido" : "Token inválido o expirado");
    }

    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite cambiar la contraseña de un usuario usando el token de recuperación",
            parameters = {
                    @Parameter(name = "token", description = "Token de recuperación", required = true),
                    @Parameter(name = "nuevaPassword", description = "Nueva contraseña", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
            }
    )
    @PostMapping("/cambiar")
    public ResponseEntity<?> cambiarPassword(@RequestParam String token, @RequestParam String nuevaPassword) {
        recoveryService.cambiarPassword(token, nuevaPassword);
        return ResponseEntity.ok("Contraseña actualizada");
    }
}
