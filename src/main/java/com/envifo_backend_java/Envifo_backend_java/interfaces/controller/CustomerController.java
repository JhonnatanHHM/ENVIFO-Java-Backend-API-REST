package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.dto.CustomerCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.CustomerDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.CustomerServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Clientes", description = "Operaciones relacionadas con los clientes")
public class CustomerController {

    private final JwtUtils jwtUtils;
    private final CustomerServiceImple customerService;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomerController(JwtUtils jwtUtils, CustomerServiceImple customerService, StorageService storageService, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.customerService = customerService;
        this.storageService = storageService;
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{idCliente}")
    public ResponseEntity<?> getCustomer(@RequestHeader("Authorization") String token,
                                         @PathVariable Long idCliente) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        Optional<CustomerDto> customerOpt = customerService.getByIdCLiente(idCliente);
        return customerOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado"));
    }

    @Operation(summary = "Obtener cliente con imágenes")
    @GetMapping("/complete/{idCliente}")
    public ResponseEntity<?> getCustomerComplete(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long idCliente) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        Optional<CustomerCompleteDto> customerOpt = customerService.getCustomerWithImages(idCliente);
        return customerOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado"));
    }

    @Operation(summary = "Obtener todos los clientes")
    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers(@RequestHeader("Authorization") String token) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Eliminar cliente")
    @DeleteMapping("/{idCliente}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String token,
                                            @PathVariable Long idCliente) throws IOException {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        customerService.delete(idCliente);
        return ResponseEntity.ok("Cliente eliminado correctamente");
    }

    @Operation(summary = "Actualizar cliente con imagen")
    @PutMapping(value = "/{idCliente}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCustomer(@RequestHeader("Authorization") String token,
                                            @PathVariable Long idCliente,
                                            @RequestPart("customer") String customerJson,
                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        try {
            CustomerDto customerDto = objectMapper.readValue(customerJson, CustomerDto.class);
            customerDto.setCustomerId(idCliente);
            customerService.editCustomer(customerDto, file);
            return ResponseEntity.ok("Cliente actualizado correctamente");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    @Operation(summary = "Subir imagen de cliente")
    @PostMapping(value = "/save/imagen/{idCliente}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirImagenCliente(@RequestHeader("Authorization") String token,
                                                @PathVariable Long idCliente,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        if (!customerService.existsByIdCliente(idCliente)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }

        StorageDto dto = new StorageDto();
        dto.setIdEntity(idCliente);

        StorageDto response = storageService.saveFile(file, dto, "cliente", "imagenes");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Eliminar imagen de cliente")
    @DeleteMapping("/imagen/{idImagen}")
    public ResponseEntity<?> deleteImageCliente(@RequestHeader("Authorization") String token,
                                                @PathVariable Long idImagen) throws IOException {
        if (!validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        storageService.deleteFileById(idImagen);
        return ResponseEntity.ok("Imagen eliminada correctamente");
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
