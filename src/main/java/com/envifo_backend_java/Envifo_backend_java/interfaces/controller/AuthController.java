package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.RegisterCustomerDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.CustomerService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.RolService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.UserService;
import com.envifo_backend_java.Envifo_backend_java.application.dto.JwtResponseDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.LoginDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RegisterDto;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Autenticación", description = "Endpoints relacionados con autenticación y autorización")
public class AuthController {

    private UserService userService;

    private RolService rolService;

    private JwtUtils jwtUtils;

    private CustomerService customerService;

    @Autowired
    public AuthController(UserService userService, RolService rolService, JwtUtils jwtUtils, CustomerService customerService) {
        this.userService = userService;
        this.rolService = rolService;
        this.jwtUtils = jwtUtils;
        this.customerService = customerService;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y retorna un token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", content = @Content(schema = @Schema(implementation = JwtResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario con rol por defecto",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existente")
            }
    )

    @PostMapping("/registerUser")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User register success!"));
    }

    @Operation(summary = "Registrar nuevo cliente")
    @PostMapping("/registerCustomer")
    public ResponseEntity<?> registerCustomer(
            @RequestBody RegisterCustomerDto registerCustomerDto) {
        try {
            customerService.registerCustomer(registerCustomerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente registrado exitosamente");
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el cliente: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Refrescar token JWT",
            description = "Genera un nuevo token basado en la autenticación actual",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token renovado exitosamente", content = @Content(schema = @Schema(implementation = JwtResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
            }
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToke(Authentication authentication) {
        String token = jwtUtils.refreshToken(authentication);
        JwtResponseDto jwtRefresh = new JwtResponseDto(token);
        return new ResponseEntity<>(jwtRefresh, HttpStatus.OK);
    }

}
