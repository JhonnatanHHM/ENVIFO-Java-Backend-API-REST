package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.RolService;
import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.UserService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.JwtResponseDom;
import com.envifo_backend_java.Envifo_backend_java.domain.model.LoginDom;
import com.envifo_backend_java.Envifo_backend_java.domain.model.RegisterDom;
import com.envifo_backend_java.Envifo_backend_java.domain.model.UserDom;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;


@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private RolService rolService;
    @Autowired
    private JwtGenerator jwtGenerator;


    @PostMapping("/login") //POST
    public ResponseEntity<JwtResponseDom> login(@RequestBody LoginDom loginDom) {
        return ResponseEntity.ok(userService.login(loginDom));
    }

    @PostMapping("/register") //POST
    public ResponseEntity<String> register(@RequestBody RegisterDom registerDom) {
        userService.register(registerDom);

        return new ResponseEntity<>("User register success!", HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token") //POST
    public ResponseEntity<?> refreshToke(Authentication authentication){

        String token = jwtGenerator.refreshToken(authentication);

        JwtResponseDom jwtRefresh = new JwtResponseDom(token);
        return new ResponseEntity<JwtResponseDom>(jwtRefresh, HttpStatus.OK);
    }

    @GetMapping("/logued")
    public ResponseEntity<UserDom> getLoguedUser(@RequestHeader HttpHeaders headers){
        return new ResponseEntity<>(userService.getLoguedUser(headers), HttpStatus.OK);
    }
}
