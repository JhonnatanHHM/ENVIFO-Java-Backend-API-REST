package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.infrastructure.config.FlywayConfig;
import org.flywaydb.core.Flyway;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/flyway")
public class FlywayController {

    private final Flyway flyway;

    private final FlywayConfig flywayConfig;

    private final JdbcTemplate jdbcTemplate;

    public FlywayController(Flyway flyway, FlywayConfig flywayConfig, JdbcTemplate jdbcTemplate) {
        this.flyway = flyway;
        this.flywayConfig = flywayConfig;
        this.jdbcTemplate = jdbcTemplate;
    }

    // Postman: http://localhost:8080/admin/flyway/migrar?seedData=true con datos
    // http://localhost:8080/admin/flyway/migrar sin datos
    @PostMapping("/migrar")
    public ResponseEntity<String> migrar(
            @RequestParam(defaultValue = "false") boolean seedData) {
        flywayConfig.migrateDatabase(seedData);
        return ResponseEntity.ok("Migración ejecutada con seedData=" + seedData);
    }

    //Postman: http://localhost:8080/admin/flyway/clean
    @PostMapping("/clean")
    public ResponseEntity<String> cleanDatabase() {
        flyway.clean();
        return ResponseEntity.ok("Base de datos limpiada correctamente");
    }

    //Postman: http://localhost:8080/admin/flyway/historial
    @GetMapping("/historial")
    public List<Map<String, Object>> verHistorial() {
        return jdbcTemplate.queryForList("SELECT * FROM flyway_schema_history ORDER BY installed_rank");
    }
}
