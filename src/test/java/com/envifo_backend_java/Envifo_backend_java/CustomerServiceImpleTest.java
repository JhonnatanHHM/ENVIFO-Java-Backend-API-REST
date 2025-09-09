package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.application.service.CustomerServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.service.RolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.*;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImpleTest {

    @Mock private ClientesRepository clientesRepository;
    @Mock private RolServiceImple rolService;
    @Mock private RolesRepository rolesRepository;
    @Mock private PermisosRepository permisosRepository;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtils jwtUtils;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AlmacenamientoRepository almacenamientoRepository;
    @Mock private StorageService storageService;
    @Mock private MultipartFile multipartFile;

    @InjectMocks
    private CustomerServiceImple customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- TEST: getCustomerWithImages ----------
    @Test
    void testGetCustomerWithImagesFound() {
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(1L);
        cliente.setNombre("Juan");
        cliente.setDireccion("Calle Falsa");
        cliente.setTelefono("123");
        cliente.setEmail("juan@test.com");
        cliente.setEstado(true);

        PermisosEntity permisos = new PermisosEntity();
        permisos.setIdPermiso(1L);

        RolesEntity rol = new RolesEntity();
        rol.setIdRol(1L);
        rol.setName("GLOBAL");
        rol.setDescription("Rol global de prueba");
        rol.setPermisos(permisos);

        cliente.setRol(rol);

        when(clientesRepository.getByIdCliente(1L)).thenReturn(Optional.of(cliente));
        when(almacenamientoRepository.findByIdEntidadAndTipoEntidad(1L, "cliente"))
                .thenReturn(Optional.empty());

        Optional<CustomerCompleteDto> result = customerService.getCustomerWithImages(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getName());
    }

    @Test
    void testGetCustomerWithImagesNotFound() {
        when(clientesRepository.getByIdCliente(99L)).thenReturn(Optional.empty());
        Optional<CustomerCompleteDto> result = customerService.getCustomerWithImages(99L);
        assertFalse(result.isPresent());
    }

    // ---------- TEST: existsByIdCliente ----------
    @Test
    void testExistsByIdCliente() {
        when(clientesRepository.existsById(1L)).thenReturn(true);
        assertTrue(customerService.existsByIdCliente(1L));
    }

    // ---------- TEST: getByIdCLiente ----------
    @Test
    void testGetByIdClienteFound() {
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(2L);
        cliente.setNombre("Maria");

        when(clientesRepository.getByIdCliente(2L)).thenReturn(Optional.of(cliente));

        Optional<CustomerDto> result = customerService.getByIdCLiente(2L);

        assertTrue(result.isPresent());
        assertEquals("Maria", result.get().getName());
    }

    @Test
    void testGetByIdClienteNotFound() {
        when(clientesRepository.getByIdCliente(10L)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> customerService.getByIdCLiente(10L));
    }

    // ---------- TEST: registerCustomer ----------
    @Test
    void testRegisterCustomerConflict() {
        RegisterCustomerDto dto = new RegisterCustomerDto();
        dto.setEmail("test@test.com");

        when(clientesRepository.getExistsByEmail("test@test.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> customerService.registerCustomer(dto));
    }

    // ---------- TEST: editCustomer ----------
    @Test
    void testEditCustomerNotFound() {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(50L);

        when(clientesRepository.getByIdCliente(50L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> customerService.editCustomer(dto, null));
    }

    // ---------- TEST: getAllCustomers ----------
    @Test
    void testGetAllCustomers() {
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(1L);
        cliente.setNombre("Carlos");

        PermisosEntity permisos = new PermisosEntity();
        RolesEntity rol = new RolesEntity();
        rol.setIdRol(1L);
        rol.setName("GLOBAL");
        rol.setPermisos(permisos);
        cliente.setRol(rol);

        when(clientesRepository.getAll()).thenReturn(Collections.singletonList(cliente));
        when(almacenamientoRepository.findByIdEntidadAndTipoEntidad(1L, "cliente"))
                .thenReturn(Optional.empty());

        List<CustomerCompleteDto> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("Carlos", result.get(0).getName());
    }

    // ---------- TEST: delete ----------
    @Test
    void testDeleteNotFound() {
        when(clientesRepository.getByIdCliente(123L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> customerService.delete(123L));
    }

    @Test
    void testDeleteSuccess() throws IOException {
        ClientesEntity cliente = new ClientesEntity();
        cliente.setIdCliente(1L);
        cliente.setEstado(true);

        when(clientesRepository.getByIdCliente(1L)).thenReturn(Optional.of(cliente));

        customerService.delete(1L);

        verify(clientesRepository, times(1)).saveCustomer(cliente);
        assertFalse(cliente.isEstado()); // debe quedar deshabilitado
    }
}
