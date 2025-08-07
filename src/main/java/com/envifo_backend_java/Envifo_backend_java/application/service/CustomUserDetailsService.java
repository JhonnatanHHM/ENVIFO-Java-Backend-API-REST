package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.CustomUserDetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userDetailService")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final ClientesRepository clienteRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository, ClientesRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar primero si es un usuario normal
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.getByEmail(email);
        if (usuarioOpt.isPresent()) {
            return new CustomUserDetails(usuarioOpt.get());
        }

        // Si no es usuario, buscar si es cliente
        Optional<ClientesEntity> cliente = clienteRepository.getByEmail(email);
        if (cliente.isPresent()) {

            return new CustomUserDetails(cliente.get());
        }

        // Si no se encuentra ni usuario ni cliente
        throw new UsernameNotFoundException("Email no registrado: " + email);
    }
}
