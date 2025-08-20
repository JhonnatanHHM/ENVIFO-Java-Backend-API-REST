package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.UserServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserDto;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    private JwtUtils jwtUtils;
    private UserServiceImple userServiceImple;
    private StorageService storageService;
    private ObjectMapper objectMapper;

    @Autowired
    public UserController(ObjectMapper objectMapper, JwtUtils jwtUtils, UserServiceImple userServiceImple, StorageService storageService) {
        this.jwtUtils = jwtUtils;
        this.userServiceImple = userServiceImple;
        this.storageService = storageService;
        this.objectMapper = objectMapper;
    }

    @Operation(
            summary = "Obtener usuario por ID",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token Bearer", required = true),
                    @Parameter(name = "idUsuario", description = "ID del usuario", required = true)
            }
    )
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> getUser(@PathVariable Long idUsuario) {

        Optional<UserDto> userDom = userServiceImple.getByIdUsuario(idUsuario);
        return userDom.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado"));
    }

    @Operation(
            summary = "Obtener usuario completo con imágenes",
            description = "Devuelve los datos completos de un usuario incluyendo sus imágenes y rol.",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token Bearer", required = true),
                    @Parameter(name = "idUsuario", description = "ID del usuario", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                            content = @Content(schema = @Schema(implementation = UserCompleteDto.class))),
                    @ApiResponse(responseCode = "401", description = "Token inválido o no autorizado",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(schema = @Schema(implementation = String.class)))
            }
    )
    @GetMapping("/complete/{idUsuario}")
    public ResponseEntity<?> getUserWithImages(
            @PathVariable Long idUsuario) {

        try {

            Optional<UserCompleteDto> userOpt = userServiceImple.getUserWithImages(idUsuario);

            return userOpt
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado"));

        } catch (Exception e) {
            // En caso de error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener el usuario: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Obtener todos los usuarios",
            parameters = @Parameter(name = "Authorization", description = "Token Bearer", required = true)
    )
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {

        List<UserDto> users = userServiceImple.getAll();
        return ResponseEntity.ok(users);
    }


    @Operation(
            summary = "Eliminar usuario por ID"
    )
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> delete(
                                    @PathVariable Long idUsuario) throws IOException {

        userServiceImple.deleteUser(idUsuario);
        return ResponseEntity.ok("Usuario eliminado con éxito");
    }

    @Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario existente y su imagen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto al actualizar usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping(value = "/{idUsuario}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editUser(

            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable Long idUsuario,

            @Parameter(
                    description = "Datos del usuario actualizado en JSON (como String)",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))
            )
            @RequestPart("user") String userJson,

            @Parameter(
                    description = "Archivo de imagen actualizado (opcional)",
                    required = false,
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
            )
            @RequestPart(value = "file", required = false) MultipartFile file

    ) {
        try {

            // Convertir JSON a DTO
            UserDto userDto = objectMapper.readValue(userJson, UserDto.class);
            userDto.setIdUsuario(idUsuario);

            // Llamar al servicio
            UserCompleteDto updatedUser = userServiceImple.editUser(userDto, file);
            return ResponseEntity.ok(updatedUser);

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar el usuario");
        }
    }


    @Operation(
            summary = "Subir imagen para usuario",
            description = "Sube una imagen asociada a un usuario determinado.",
            parameters = {
                    @Parameter(name = "Authorization", description = "Token Bearer", required = true),
                    @Parameter(name = "idUsuario", description = "ID del usuario", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "201", description = "Imagen subida exitosamente",
                            content = @Content(schema = @Schema(implementation = StorageDto.class))),
                    @ApiResponse(responseCode = "401", description = "Token inválido o no autorizado",
                            content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(schema = @Schema(type = "string"))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(schema = @Schema(type = "string")))
            }
    )
    @PostMapping(value = "/save/imagen/{idUsuario}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirImagenUsuario(
            @PathVariable Long idUsuario,
            @Parameter(description = "Archivo de imagen", required = true, content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "string", format = "binary")
            ))
            @RequestParam("file") MultipartFile file) throws IOException {


        if (!userServiceImple.existsById(idUsuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        StorageDto dto = new StorageDto();
        dto.setIdEntity(idUsuario);

        StorageDto response = storageService.saveFile(file, dto, "usuario","imagenes");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Eliminar imagen de usuario"
    )
    @DeleteMapping("/imagen/{idImagen}")
    public ResponseEntity<?> deletePictureUser(@PathVariable Long idImagen) throws IOException {

        storageService.deleteFileById(idImagen);

        return ResponseEntity.ok("Imagen eliminada correctamente");
    }

}
