package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserDto;
import com.envifo_backend_java.Envifo_backend_java.application.service.CustomerUserRolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.service.RolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerUserRolServiceImpleTest {

    @Mock
    private CustomerUserRolRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RolServiceImple rolServiceImple;

    @Mock
    private PermissionsRepository permissionsRepository;

    @InjectMocks
    private CustomerUserRolServiceImple service;

    private UsuarioEntity usuario;
    private ClientesEntity cliente;
    private RolesEntity rol;
    private PermisosEntity permisos;
    private ClienteUsuarioRolEntity assignment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new UsuarioEntity();
        usuario.setIdUsuario(1L);
        usuario.setUserName("testUser");

        cliente = new ClientesEntity();
        cliente.setIdCliente(1L);
        cliente.setNombre("Test Cliente");

        permisos = new PermisosEntity();
        permisos.setIdPermiso(100L);

        rol = new RolesEntity();
        rol.setIdRol(10L);
        rol.setName("ADMIN");
        rol.setPermisos(permisos);

        assignment = new ClienteUsuarioRolEntity();
        assignment.setIdCliUsuRol(50L);
        assignment.setUsuario(usuario);
        assignment.setCliente(cliente);
        assignment.setRol(rol);
    }

    @Test
    void testAssignRolToUser_Success() {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(10L);

        when(userRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(customerRepository.getByIdCliente(1L)).thenReturn(Optional.of(cliente));
        when(rolServiceImple.createRol(rolDto)).thenReturn(rolDto);
        when(rolRepository.getByIdRol(10L)).thenReturn(Optional.of(rol));
        when(repository.getByUsuarioIdUsuarioAndClienteIdCliente(1L, 1L)).thenReturn(Optional.empty());

        service.assignRolToUser(1L, 1L, rolDto);

        verify(repository, times(1)).saveClienteUsuarioRolEntity(any(ClienteUsuarioRolEntity.class));
    }

    @Test
    void testAssignRolToUser_AlreadyExists() {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(10L);

        when(userRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(customerRepository.getByIdCliente(1L)).thenReturn(Optional.of(cliente));
        when(rolServiceImple.createRol(rolDto)).thenReturn(rolDto);
        when(rolRepository.getByIdRol(10L)).thenReturn(Optional.of(rol));
        when(repository.getByUsuarioIdUsuarioAndClienteIdCliente(1L, 1L)).thenReturn(Optional.of(assignment));

        assertThrows(RuntimeException.class, () -> service.assignRolToUser(1L, 1L, rolDto));
    }

    @Test
    void testGetUserRolIntoCustomer() {
        when(repository.getByUsuarioIdUsuarioAndClienteIdCliente(1L, 1L)).thenReturn(Optional.of(assignment));

        Optional<UserDto> result = service.getUserRolIntoCustomer(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals("testUser", result.get().getUserName());
    }

    @Test
    void testGetRolByCustomer() {
        when(repository.getByClienteIdCliente(1L)).thenReturn(List.of(assignment));

        List<UserDto> result = service.getRolByCustomer(1L);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUserName());
    }

    @Test
    void testDeleteAssignment_Success() {
        when(repository.getByIdCliUsuRol(50L)).thenReturn(Optional.of(assignment));

        service.deleteAssignment(50L);

        verify(repository, times(1)).deleteById(50L);
        verify(rolServiceImple, times(1)).deleteByIdRol(10L);
        verify(permissionsRepository, times(1)).deleteByIdPermiso(100L);
    }

    @Test
    void testDeleteAssignment_NotFound() {
        when(repository.getByIdCliUsuRol(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.deleteAssignment(999L));
    }

    @Test
    void testUpdateUserRolIntoCustomer_Success() {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(10L);

        when(userRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(customerRepository.getByIdCliente(1L)).thenReturn(Optional.of(cliente));
        when(repository.getByUsuarioIdUsuarioAndClienteIdCliente(1L, 1L)).thenReturn(Optional.of(assignment));
        when(rolServiceImple.editRol(rolDto)).thenReturn(rolDto);
        when(rolRepository.getByIdRol(10L)).thenReturn(Optional.of(rol));

        service.updateUserRolIntoCustomer(1L, 1L, rolDto);

        verify(repository, times(1)).saveClienteUsuarioRolEntity(any(ClienteUsuarioRolEntity.class));
    }
}
