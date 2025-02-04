package com.envifo_backend_java.Envifo_backend_java.interfaces.controller;

import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.RolService;
import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.UserService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.JwtResponseDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.LoginDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.RegisterDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.UserDto;
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
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @PostMapping("/register") //POST
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);

        return new ResponseEntity<>("User register success!", HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token") //POST
    public ResponseEntity<?> refreshToke(Authentication authentication){

        String token = jwtGenerator.refreshToken(authentication);

        JwtResponseDto jwtRefresh = new JwtResponseDto(token);
        return new ResponseEntity<JwtResponseDto>(jwtRefresh, HttpStatus.OK);
    }

    @GetMapping("/logued")
    public ResponseEntity<UserDto> getLoguedUser(@RequestHeader HttpHeaders headers){
        return new ResponseEntity<>(userService.getLoguedUser(headers), HttpStatus.OK);
    }
}
