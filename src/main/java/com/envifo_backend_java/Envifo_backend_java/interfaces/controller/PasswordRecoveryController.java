package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.PasswordRecoveryServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recuperacion")
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryServiceImpl recoveryService;

    //Ejemplo petición:
    //http://localhost:8080/api/recuperacion/solicitar?email=hhmjhonatan@gmail.com
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarToken(@RequestParam String email) throws MessagingException {
        recoveryService.iniciarRecuperacion(email) ;
        return ResponseEntity.ok("Correo enviado");
    }

    //Ejemplo petición:
    //http://localhost:8080/api/recuperacion/cambiar?token=4ac9b692-c417-4c8d-a574-410e4f5cc932
    @GetMapping("/validar")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        boolean valido = recoveryService.validarToken(token);
        return ResponseEntity.ok(valido ? "Token válido" : "Token inválido o expirado");
    }

    //Ejemplo petición:
    //http://localhost:8080/api/recuperacion/cambiar?token=7df4e273-6961-4a28-aff5-645349c8cced&nuevaPassword=jhonatan123
    @PostMapping("/cambiar")
    public ResponseEntity<?> cambiarPassword(@RequestParam String token, @RequestParam String nuevaPassword) {
        recoveryService.cambiarPassword(token, nuevaPassword);
        return ResponseEntity.ok("Contraseña actualizada");
    }
}

