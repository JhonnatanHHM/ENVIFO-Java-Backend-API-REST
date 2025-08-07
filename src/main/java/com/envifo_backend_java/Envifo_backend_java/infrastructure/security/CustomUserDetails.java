package com.envifo_backend_java.Envifo_backend_java.infrastructure.security;

import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClientesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private UsuarioEntity usuario;
    private ClientesEntity cliente;
    private Collection<? extends GrantedAuthority> authorities;

    // Constructor para Usuario
    public CustomUserDetails(UsuarioEntity usuario) {
        this.usuario = usuario;
        this.authorities = List.of(new SimpleGrantedAuthority(usuario.getRol().getName()));
    }

    // Constructor para Cliente
    public CustomUserDetails(ClientesEntity cliente) {
        this.cliente = cliente;
        this.authorities = List.of(new SimpleGrantedAuthority(cliente.getRol().getName()));
    }

    // Métodos auxiliares
    public boolean esUsuario() {
        return usuario != null;
    }

    public boolean esCliente() {
        return cliente != null;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public ClientesEntity getCliente() {
        return cliente;
    }

    // Métodos requeridos por UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return esUsuario() ? usuario.getPassword() : cliente.getPassword();
    }

    @Override
    public String getUsername() {
        return esUsuario() ? usuario.getEmail() : cliente.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return esUsuario() ? usuario.isEstado() : cliente.isEstado();
    }
}
