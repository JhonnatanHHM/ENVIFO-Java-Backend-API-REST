package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyTokenController {

    @RequestMapping("/token") //GET
    public String token() {
        return "Funciona el token de acceso!";
    }

    @RequestMapping("/admin") //GET
    public String admin() {
        return "Bienvenido Admin!";
    }

    @RequestMapping("/user") //GET
    public String user() {
        return "Bienvenido User!";
    }
}