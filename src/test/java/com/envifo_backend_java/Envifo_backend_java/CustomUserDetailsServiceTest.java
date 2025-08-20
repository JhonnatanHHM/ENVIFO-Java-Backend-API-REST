package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.service.CustomUserDetailsService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClientesRepository clientesRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    private UsuarioEntity usuario;
    private ClientesEntity cliente;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // --- Usuario con rol ---
        usuario = new UsuarioEntity();
        usuario.setIdUsuario(1L);
        usuario.setEmail("usuario@test.com");
        usuario.setUserName("usuario1");
        usuario.setPassword("password");

        RolesEntity rolUsuario = new RolesEntity();
        rolUsuario.setIdRol(1L);
        rolUsuario.setName("ROLE_USER");

        // si tu modelo requiere permisos:
        PermisosEntity permisos = new PermisosEntity();
        permisos.setIdPermiso(1L);
        rolUsuario.setPermisos(permisos);

        usuario.setRol(rolUsuario);

        // --- Cliente con rol ---
        cliente = new ClientesEntity();
        cliente.setIdCliente(2L);
        cliente.setEmail("cliente@test.com");
        cliente.setNombre("Cliente1");
        cliente.setPassword("password");

        RolesEntity rolCliente = new RolesEntity();
        rolCliente.setIdRol(2L);
        rolCliente.setName("ROLE_CLIENT");

        PermisosEntity permisosCliente = new PermisosEntity();
        permisosCliente.setIdPermiso(2L);
        rolCliente.setPermisos(permisosCliente);

        cliente.setRol(rolCliente);
    }

    @Test
    void testLoadUserByUsername_FoundAsUsuario() {
        when(usuarioRepository.getByEmail("usuario@test.com")).thenReturn(Optional.of(usuario));

        UserDetails result = service.loadUserByUsername("usuario@test.com");

        assertNotNull(result);
        assertTrue(result instanceof CustomUserDetails);
        assertEquals("usuario@test.com", result.getUsername());
        verify(usuarioRepository, times(1)).getByEmail("usuario@test.com");
        verifyNoInteractions(clientesRepository);
    }

    @Test
    void testLoadUserByUsername_FoundAsCliente() {
        when(usuarioRepository.getByEmail("cliente@test.com")).thenReturn(Optional.empty());
        when(clientesRepository.getByEmail("cliente@test.com")).thenReturn(Optional.of(cliente));

        UserDetails result = service.loadUserByUsername("cliente@test.com");

        assertNotNull(result);
        assertTrue(result instanceof CustomUserDetails);
        assertEquals("cliente@test.com", result.getUsername());
        verify(usuarioRepository, times(1)).getByEmail("cliente@test.com");
        verify(clientesRepository, times(1)).getByEmail("cliente@test.com");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(usuarioRepository.getByEmail("notfound@test.com")).thenReturn(Optional.empty());
        when(clientesRepository.getByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("notfound@test.com"));

        verify(usuarioRepository, times(1)).getByEmail("notfound@test.com");
        verify(clientesRepository, times(1)).getByEmail("notfound@test.com");
    }
}
