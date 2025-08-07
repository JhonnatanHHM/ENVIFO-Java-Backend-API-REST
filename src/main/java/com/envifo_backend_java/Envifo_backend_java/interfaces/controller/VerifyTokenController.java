package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verificacion")
@Tag(name = "Verificación de Token", description = "Endpoints para verificar roles y token JWT")
public class VerifyTokenController {

    private JwtUtils jwtUtils;

    @Autowired
    public VerifyTokenController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Operation(
            summary = "Verificar acceso por token JWT",
            description = "Verifica si el token JWT es válido y se permite el acceso",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token válido")
            }
    )
    @GetMapping("/token")
    public String token(){
        return "Funciona el token de acceso!";
    }

    @Operation(
            summary = "Verificar acceso como administrador",
            description = "Verifica si el usuario autenticado tiene rol de administrador",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Acceso permitido como administrador")
            }
    )
    @GetMapping("/admin")
    public String admin() {
        return "Bienvenido Admin!";
    }

    @Operation(
            summary = "Verificar acceso como usuario",
            description = "Verifica si el usuario autenticado tiene rol de usuario estándar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Acceso permitido como usuario")
            }
    )
    @GetMapping("/user")
    public String user() {
        return "Bienvenido User!";
    }

}
