package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteUsuarioRolEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.service.CustomerUserRolService;
import com.envifo_backend_java.Envifo_backend_java.application.service.RolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Gestión de Roles", description = "Operaciones para asignar, consultar, eliminar y editar roles de usuarios en clientes.")
public class RolController {

    private final CustomerUserRolService customerUserRolService;
    private final RolServiceImple rolServiceImple;
    private final JwtUtils jwtUtils;

    @Autowired
    public RolController(CustomerUserRolService customerUserRolService,
                         RolServiceImple rolServiceImple,
                         JwtUtils jwtUtils) {
        this.customerUserRolService = customerUserRolService;
        this.rolServiceImple = rolServiceImple;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
            summary = "Asignar rol a usuario en cliente",
            description = "Asigna un nuevo rol a un usuario en el contexto de un cliente.",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
                    @Parameter(name = "idUsuario", description = "ID del usuario", required = true),
                    @Parameter(name = "idCliente", description = "ID del cliente", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol asignado correctamente"),
                    @ApiResponse(responseCode = "401", description = "Token inválido"),
                    @ApiResponse(responseCode = "500", description = "Error interno")
            }
    )
    @PostMapping("/usuario/{idUsuario}/cliente/{idCliente}")
    public ResponseEntity<?> asignarRol(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable Long idUsuario,
            @PathVariable Long idCliente,
            @RequestBody RolDto rolDto) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }

        customerUserRolService.assignRolToUser(idUsuario, idCliente, rolDto);
        return ResponseEntity.ok("Rol asignado correctamente al usuario en el cliente.");
    }

    @Operation(
            summary = "Obtener rol del usuario en cliente",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
                    @Parameter(name = "idUsuario", description = "ID del usuario", required = true),
                    @Parameter(name = "idCliente", description = "ID del cliente", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido"),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
            }
    )
    @GetMapping("/usuario/{idUsuario}/cliente/{idCliente}")
    public ResponseEntity<?> obtenerRolUsuarioCliente(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable Long idUsuario,
            @PathVariable Long idCliente) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }

        Optional<UserDto> userDto = customerUserRolService.getUserRolIntoCustomer(idUsuario, idCliente);

        return userDto
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado"));
    }

    @Operation(
            summary = "Obtener todos los roles asignados a un cliente",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
                    @Parameter(name = "idCliente", description = "ID del cliente", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida"),
                    @ApiResponse(responseCode = "401", description = "Token inválido")
            }
    )
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> obtenerRolesPorCliente(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable Long idCliente) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }

        List<UserDto> lista = customerUserRolService.getRolByCustomer(idCliente);

        return ResponseEntity.ok(lista);
    }

    @Operation(
            summary = "Eliminar una asignación de rol",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
                    @Parameter(name = "idAsignacion", description = "ID de la asignación a eliminar", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Asignación eliminada"),
                    @ApiResponse(responseCode = "401", description = "Token inválido")
            }
    )
    @DeleteMapping("/{idAsignacion}")
    public ResponseEntity<?> eliminarAsignacion(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable Long idAsignacion) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }

        customerUserRolService.deleteAssignment(idAsignacion);
        return ResponseEntity.ok("Asignación eliminada correctamente.");
    }

    @Operation(
            summary = "Obtener el rol GLOBAL",
            parameters = @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol GLOBAL encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido"),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
            }
    )
    @GetMapping("/rol/GLOBAL")
    public ResponseEntity<?> getRolGlobal(@RequestHeader(value = "Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }

        return rolServiceImple.getByname("GLOBAL")
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "Obtener el rol RESTRINGIDO",
            parameters = @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol RESTRINGIDO encontrado"),
                    @ApiResponse(responseCode = "401", description = "Token inválido"),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
            }
    )
    @GetMapping("/rol/RESTRINGIDO")
    public ResponseEntity<?> getRolRestringido(@RequestHeader(value = "Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido"));
        }

        return rolServiceImple.getByname("RESTRINGIDO")
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @Operation(
            summary = "Actualizar el rol de un usuario en un cliente",
            description = "Permite actualizar el rol que un usuario tiene asignado dentro de un cliente.",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token JWT Bearer", required = true),
                    @Parameter(name = "idUsuario", description = "ID del usuario", required = true),
                    @Parameter(name = "idCliente", description = "ID del cliente", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
                    @ApiResponse(responseCode = "401", description = "Token inválido o no proporcionado"),
                    @ApiResponse(responseCode = "404", description = "Usuario, cliente o asignación no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno al actualizar el rol")
            }
    )
    @PutMapping("/usuario/{idUsuario}/cliente/{idCliente}")
    public ResponseEntity<?> actualizarRolUsuarioCliente(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable Long idUsuario,
            @PathVariable Long idCliente,
            @RequestBody RolDto rolDto
    ) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token inválido o no proporcionado"));
        }

        try {
            customerUserRolService.updateUserRolIntoCustomer(idUsuario, idCliente, rolDto);
            return ResponseEntity.ok("Rol del usuario actualizado correctamente.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar el rol: " + ex.getMessage()));
        }
    }


    private boolean validarToken(String token) {
        try {
            token = token.replace("Bearer ", "").trim();
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }
}
