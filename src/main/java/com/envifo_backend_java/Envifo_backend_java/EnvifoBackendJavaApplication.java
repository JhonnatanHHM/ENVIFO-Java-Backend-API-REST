package com.envifo_backend_java.Envifo_backend_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnvifoBackendJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnvifoBackendJavaApplication.class, args);
	}

}
