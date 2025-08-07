package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.config.FlywayConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flywaydb.core.Flyway;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/flyway")
@Tag(name = "Administración de Flyway", description = "Controlador para ejecutar migraciones, limpiar la base de datos y ver historial de Flyway")
public class FlywayController {

    private final Flyway flyway;
    private final FlywayConfig flywayConfig;
    private final JdbcTemplate jdbcTemplate;

    public FlywayController(Flyway flyway, FlywayConfig flywayConfig, JdbcTemplate jdbcTemplate) {
        this.flyway = flyway;
        this.flywayConfig = flywayConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Operation(
            summary = "Ejecutar migraciones con o sin datos de prueba",
            description = "Ejecuta migraciones en la base de datos, y opcionalmente si `seedData=true`, también inserta datos iniciales.",
            parameters = {
                    @Parameter(name = "seedData", description = "Indica si se deben insertar datos de prueba", example = "true")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Migración ejecutada correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error al ejecutar la migración", content = @Content)
            }
    )
    @PostMapping("/migrar")
    public ResponseEntity<String> migrar(@RequestParam(defaultValue = "false") boolean seedData) {
        flywayConfig.migrateDatabase(seedData);
        return ResponseEntity.ok("Migración ejecutada con seedData=" + seedData);
    }

    @Operation(
            summary = "Limpiar base de datos",
            description = "Elimina todas las tablas y metadatos de la base de datos usando Flyway.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Base de datos limpiada correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error al limpiar la base de datos", content = @Content)
            }
    )
    @PostMapping("/clean")
    public ResponseEntity<String> cleanDatabase() {
        flyway.clean();
        return ResponseEntity.ok("Base de datos limpiada correctamente");
    }

    @Operation(
            summary = "Ver historial de migraciones",
            description = "Consulta y retorna el historial de migraciones ejecutadas por Flyway.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "Error al obtener el historial", content = @Content)
            }
    )
    @GetMapping("/historial")
    public List<Map<String, Object>> verHistorial() {
        return jdbcTemplate.queryForList("SELECT * FROM flyway_schema_history ORDER BY installed_rank");
    }
}
