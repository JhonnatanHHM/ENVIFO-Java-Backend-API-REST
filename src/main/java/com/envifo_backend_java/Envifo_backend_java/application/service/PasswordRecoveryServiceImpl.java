package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PasswordTokenEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ContraseñaTokenRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordRecoveryServiceImpl{

    @Autowired
    private ContraseñaTokenRepository tokenRepo;

    @Autowired
    private EmailServiceImple emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void iniciarRecuperacion(String email) throws MessagingException {
        // Verifica si el usuario existe por email
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.getByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("El correo no está registrado en la aplicación");
        }

        UsuarioEntity usuario = usuarioOpt.get();

        // Genera el token
        String token = UUID.randomUUID().toString();

        PasswordTokenEntity tokenEntity = new PasswordTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setEmail(email);
        tokenEntity.setExpiration(LocalDateTime.now().plusHours(1));
        tokenEntity.setUsed(false);

        // Guarda el token
        tokenRepo.save(tokenEntity);

        // Prepara la URL de recuperación con el token como parámetro
        String url = "http://localhost:8080/api/recuperacion/cambiar?token=" + token;

        // Envia el correo con el nombre del usuario y la URL personalizada
        emailService.enviarCorreoRecuperacion(email, usuario.getNombre(), url);
    }

    public boolean validarToken(String token) {
        return tokenRepo.GetByToken(token)
                .filter(t -> !t.isUsed() && t.getExpiration().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public void cambiarPassword(String token, String nuevaPassword) {
        PasswordTokenEntity tokenEntity = tokenRepo.GetByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (tokenEntity.isUsed() || tokenEntity.getExpiration().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado o ya usado");
        }

        // aquí deberías buscar al usuario por email y cambiarle la contraseña
        UsuarioEntity usuario = usuarioRepository.getByEmail(tokenEntity.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);

        tokenEntity.setUsed(true);
        tokenRepo.save(tokenEntity);
    }
}
