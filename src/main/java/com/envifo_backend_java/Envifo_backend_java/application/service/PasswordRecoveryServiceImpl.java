package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PasswordTokenEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ContraseñaTokenRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


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

    @Autowired
    private ClientesRepository clienteRepository;

    public void iniciarRecuperacion(String email) throws MessagingException {
        // Buscar primero si es un usuario normal
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.getByEmail(email);

        String nombreCompleto;

        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();
            nombreCompleto = usuario.getPrimerNombre() + " " + usuario.getPrimerApellido();
        } else {
            // Si no es usuario, buscar si es cliente
            Optional<ClientesEntity> clienteOpt = clienteRepository.getByEmail(email);
            if (clienteOpt.isEmpty()) {
                throw new NotFoundException("El e-mail no se encuentra registrado en la aplicación");
            }

            ClientesEntity cliente = clienteOpt.get();
            nombreCompleto = cliente.getNombre();
        }

        // Generar token
        String token = generateToken();

        PasswordTokenEntity tokenEntity = new PasswordTokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setEmail(email);
        tokenEntity.setExpiration(LocalDateTime.now().plusMinutes(15));
        tokenEntity.setUsed(false);

        // Guarda el token
        tokenRepo.save(tokenEntity);

        // Prepara la URL de recuperación con el token como parámetro
        String url = /* "http://localhost:8080/api/recuperacion/cambiar?token=" + */ token;

        // Enviar correo de recuperación
        emailService.enviarCorreoRecuperacion(email, nombreCompleto, url);
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

        String email = tokenEntity.getEmail();
        boolean passwordUpdated = false;

        // Buscar si es un usuario
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.getByEmail(email);
        if (usuarioOpt.isPresent()) {
            UsuarioEntity usuario = usuarioOpt.get();
            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(usuario);
            passwordUpdated = true;
        }

        // Si no fue usuario, buscar si es un cliente
        if (!passwordUpdated) {
            Optional<ClientesEntity> clienteOpt = clienteRepository.getByEmail(email);
            if (clienteOpt.isPresent()) {
                ClientesEntity cliente = clienteOpt.get();
                cliente.setPassword(passwordEncoder.encode(nuevaPassword));
                clienteRepository.saveCustomer(cliente);
                passwordUpdated = true;
            }
        }

        if (!passwordUpdated) {
            throw new RuntimeException("No se encontró un usuario ni cliente con ese email");
        }

        // Marcar el token como usado
        tokenEntity.setUsed(true);
        tokenRepo.save(tokenEntity);
    }


    // Ejecuta cada hora
    @Transactional
    @Scheduled(cron = "0 0 * * * *") // (segundo, minuto, hora, día, mes, día_semana)
    public void eliminarTokensExpirados() {
        tokenRepo.deleteByExpirationBefore(LocalDateTime.now());
        System.out.println("Se eliminaron tokens expirados: " + LocalDateTime.now());
    }

    private String generateToken() {
        Random random = new Random();
        int parte1 = 100 + random.nextInt(900); // número entre 100 y 999
        int parte2 = 100 + random.nextInt(900); // número entre 100 y 999
        return parte1 + "-" + parte2;
    }
}
