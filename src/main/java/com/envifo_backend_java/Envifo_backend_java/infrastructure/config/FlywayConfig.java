package com.envifo_backend_java.Envifo_backend_java.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    private final DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                /*
                ATENCION!!! 🛑
                Esta lÍnea solo se debe utilizar para hacer pruebas
                Permite eliminar la base de datos y normalmente debe estar con valor de TRUE ✅
                */
                .cleanDisabled(false)
                .load();
    }

    public void migrateDatabase(boolean seedData) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(
                        "classpath:db/migration",
                        seedData ? "classpath:db/testdata" : "classpath:db/empty"
                )
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();
    }
}
