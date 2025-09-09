package com.envifo_backend_java.Envifo_backend_java;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.application.service.RolServiceImple;
import com.envifo_backend_java.Envifo_backend_java.application.service.UserServiceImple;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CustomerRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.*;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtTokenFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImpleTest {

    @Mock
    private StorageService storageService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolServiceImple rolService;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PermisosRepository permisosRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenFactory jwtTokenFactory;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AlmacenamientoRepository almacenamientoRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private UserServiceImple userService;

    private UsuarioEntity usuario;
    private AlmacenamientoEntity almacenamiento;

    @BeforeEach
    void setUp() throws IOException {
        usuario = new UsuarioEntity();
        usuario.setIdUsuario(1L);
        usuario.setPrimerNombre("Juan");
        usuario.setPrimerApellido("Perez");
        usuario.setEmail("juan@email.com");
        usuario.setEstado(true);

        almacenamiento = new AlmacenamientoEntity();
        almacenamiento.setIdArchivo(10L);
        almacenamiento.setNombreArchivo("foto.png");
        almacenamiento.setIdEntidad(1L);

        lenient().when(multipartFile.getInputStream())
                .thenReturn(new ByteArrayInputStream("data".getBytes()));
        lenient().when(multipartFile.getOriginalFilename()).thenReturn("file.png");
        lenient().when(multipartFile.isEmpty()).thenReturn(false);
    }

    @Test
    void getUserWithImages_ShouldReturnUserCompleteDto() throws IOException {
        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(almacenamientoRepository.findByIdEntidadAndTipoEntidad(1L, "usuario"))
                .thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(10L)).thenReturn("https://example.com/file.png");

        Optional<UserCompleteDto> result = userService.getUserWithImages(1L);

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getFirstName());
        assertEquals("https://example.com/file.png", result.get().getImages().get().getKeyR2());
    }

    @Test
    void register_ShouldCreateUser_WhenEmailNotExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("nuevo@email.com");
        registerDto.setPassword("1234");
        registerDto.setFirstName("Nuevo");
        registerDto.setFirstSurname("Usuario");

        when(usuarioRepository.getExistsByEmail("nuevo@email.com")).thenReturn(false);
        when(customerRepository.getExistsByEmail("nuevo@email.com")).thenReturn(false); // 🔹 Mock agregado
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        // Simular que no existe el rol y se crea
        when(rolesRepository.getByName("RESTRINGIDO")).thenReturn(Optional.empty());
        when(permisosRepository.save(any(PermisosEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        RolesEntity rolEntity = new RolesEntity();
        rolEntity.setName("RESTRINGIDO");
        rolEntity.setDescription("Usuario general");
        when(rolesRepository.save(any(RolesEntity.class))).thenReturn(rolEntity);

        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.register(registerDto);

        assertEquals("Nuevo", result.getFirstName());
        assertEquals("Usuario general", result.getRol().getDescription());
    }

    @Test
    void register_ShouldThrowConflictException_WhenEmailExists() {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setEmail("existente@email.com");

        when(usuarioRepository.getExistsByEmail("existente@email.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.register(registerDto));
    }


    @Test
    void deleteUser_ShouldDisableUser() throws IOException {
        usuario.setEstado(true);
        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));

        userService.deleteUser(1L);

        assertFalse(usuario.isEstado());
        verify(usuarioRepository).save(usuario);
        verify(storageService).deleteFileByEntity(1L, "usuario");
    }

    @Test
    void editUser_ShouldUpdateUserDataAndImage() throws IOException {
        UserDto userDto = new UserDto();
        userDto.setIdUsuario(1L);
        userDto.setFirstName("JuanUpdated");

        when(usuarioRepository.getByIdUsuario(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);
        when(almacenamientoRepository.findByIdEntidadAndTipoEntidad(1L, "usuario"))
                .thenReturn(Optional.of(almacenamiento));
        when(storageService.getPresignedUrl(10L)).thenReturn("https://example.com/file.png");

        UserCompleteDto result = userService.editUser(userDto, multipartFile);

        assertEquals("JuanUpdated", result.getFirstName());
        verify(storageService).updateFile(10L, multipartFile, "imagenes");
    }
}
