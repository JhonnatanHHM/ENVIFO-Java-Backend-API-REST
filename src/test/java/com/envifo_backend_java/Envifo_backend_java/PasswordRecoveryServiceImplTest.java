package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.service.EmailServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.service.PasswordRecoveryServiceImpl;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PasswordTokenEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ContraseñaTokenRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordRecoveryServiceImplTest {

    @InjectMocks
    private PasswordRecoveryServiceImpl service;

    @Mock
    private ContraseñaTokenRepository tokenRepo;

    @Mock
    private EmailServiceImple emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClientesRepository clienteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- iniciarRecuperacion ---
    @Test
    void testIniciarRecuperacion_usuario() throws MessagingException {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setPrimerNombre("Juan");
        usuario.setPrimerApellido("Pérez");

        when(usuarioRepository.getByEmail("user@test.com")).thenReturn(Optional.of(usuario));

        service.iniciarRecuperacion("user@test.com");

        verify(tokenRepo, times(1)).save(any(PasswordTokenEntity.class));
        verify(emailService, times(1))
                .enviarCorreoRecuperacion(eq("user@test.com"), eq("Juan Pérez"), anyString());
    }

    @Test
    void testIniciarRecuperacion_cliente() throws MessagingException {
        when(usuarioRepository.getByEmail("cliente@test.com")).thenReturn(Optional.empty());

        ClientesEntity cliente = new ClientesEntity();
        cliente.setNombre("Empresa X");
        when(clienteRepository.getByEmail("cliente@test.com")).thenReturn(Optional.of(cliente));

        service.iniciarRecuperacion("cliente@test.com");

        verify(tokenRepo).save(any(PasswordTokenEntity.class));
        verify(emailService)
                .enviarCorreoRecuperacion(eq("cliente@test.com"), eq("Empresa X"), anyString());
    }

    @Test
    void testIniciarRecuperacion_emailNoExiste() throws MessagingException {
        when(usuarioRepository.getByEmail("noexiste@test.com")).thenReturn(Optional.empty());
        when(clienteRepository.getByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                service.iniciarRecuperacion("noexiste@test.com")
        );

        verify(tokenRepo, never()).save(any());
        verify(emailService, never()).enviarCorreoRecuperacion(any(), any(), any());
    }

    // --- validarToken ---
    @Test
    void testValidarToken_valido() {
        PasswordTokenEntity token = new PasswordTokenEntity();
        token.setUsed(false);
        token.setExpiration(LocalDateTime.now().plusMinutes(10));

        when(tokenRepo.GetByToken("123-456")).thenReturn(Optional.of(token));

        assertTrue(service.validarToken("123-456"));
    }

    @Test
    void testValidarToken_invalido() {
        PasswordTokenEntity token = new PasswordTokenEntity();
        token.setUsed(true);
        token.setExpiration(LocalDateTime.now().plusMinutes(10));

        when(tokenRepo.GetByToken("123-456")).thenReturn(Optional.of(token));

        assertFalse(service.validarToken("123-456"));
    }

    // --- cambiarPassword ---
    @Test
    void testCambiarPassword_usuario() {
        PasswordTokenEntity token = new PasswordTokenEntity();
        token.setEmail("user@test.com");
        token.setExpiration(LocalDateTime.now().plusMinutes(10));
        token.setUsed(false);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setEmail("user@test.com");

        when(tokenRepo.GetByToken("token")).thenReturn(Optional.of(token));
        when(usuarioRepository.getByEmail("user@test.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");

        service.cambiarPassword("token", "newPass");

        assertTrue(token.isUsed());
        verify(usuarioRepository).save(usuario);
        verify(tokenRepo).save(token);
    }

    @Test
    void testCambiarPassword_cliente() {
        PasswordTokenEntity token = new PasswordTokenEntity();
        token.setEmail("cliente@test.com");
        token.setExpiration(LocalDateTime.now().plusMinutes(10));
        token.setUsed(false);

        ClientesEntity cliente = new ClientesEntity();
        cliente.setEmail("cliente@test.com");

        when(tokenRepo.GetByToken("token")).thenReturn(Optional.of(token));
        when(usuarioRepository.getByEmail("cliente@test.com")).thenReturn(Optional.empty());
        when(clienteRepository.getByEmail("cliente@test.com")).thenReturn(Optional.of(cliente));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");

        service.cambiarPassword("token", "newPass");

        assertTrue(token.isUsed());
        verify(clienteRepository).saveCustomer(cliente);
        verify(tokenRepo).save(token);
    }

    @Test
    void testCambiarPassword_tokenInvalido() {
        when(tokenRepo.GetByToken("token")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                service.cambiarPassword("token", "newPass")
        );
    }

    @Test
    void testCambiarPassword_tokenExpirado() {
        PasswordTokenEntity token = new PasswordTokenEntity();
        token.setEmail("user@test.com");
        token.setExpiration(LocalDateTime.now().minusMinutes(1));
        token.setUsed(false);

        when(tokenRepo.GetByToken("token")).thenReturn(Optional.of(token));

        assertThrows(RuntimeException.class, () ->
                service.cambiarPassword("token", "newPass")
        );
    }

    // --- eliminarTokensExpirados ---
    @Test
    void testEliminarTokensExpirados() {
        service.eliminarTokensExpirados();
        verify(tokenRepo, times(1)).deleteByExpirationBefore(any(LocalDateTime.class));
    }
}
