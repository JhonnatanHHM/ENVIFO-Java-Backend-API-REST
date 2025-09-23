package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.AlmacenamientoEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.CustomerRepository;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.domain.service.UserService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.JwtAuthenticationException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.AlmacenamientoRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.PermisosRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.RolesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.CustomUserDetails;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtUtils;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImple implements UserService {

    private StorageService storageService;

    private UsuarioRepository usuarioRepository;

    private RolServiceImple rolService;

    private RolesRepository rolesRepository;

    private PermisosRepository permisosRepository;

    private AuthenticationManager authenticationManager;

    private JwtTokenFactory jwtTokenFactory;

    private PasswordEncoder passwordEncoder;

    private AlmacenamientoRepository almacenamientoRepository;

    private CustomerRepository customerRepository;

    @Autowired
    public UserServiceImple(StorageService storageService, UsuarioRepository usuarioRepository, RolServiceImple rolService, RolesRepository rolesRepository, PermisosRepository permisosRepository, AuthenticationManager authenticationManager, JwtTokenFactory jwtTokenFactory, PasswordEncoder passwordEncoder, AlmacenamientoRepository almacenamientoRepository, CustomerRepository customerRepository) {
        this.storageService = storageService;
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
        this.rolesRepository = rolesRepository;
        this.permisosRepository = permisosRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenFactory = jwtTokenFactory;
        this.passwordEncoder = passwordEncoder;
        this.almacenamientoRepository = almacenamientoRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<UserCompleteDto> getUserWithImages(Long idUsuario) {
        // Buscar el usuario
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.getByIdUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        UsuarioEntity usuario = usuarioOpt.get();

        // Buscar la imagen (solo una) asociada al usuario
        Optional<AlmacenamientoEntity> imagenOpt = almacenamientoRepository
                .findByIdEntidadAndTipoEntidad(idUsuario, "usuario");

        Optional<StorageDto> imagenDto = imagenOpt.map(imagen -> {
            StorageDto storageDto = new StorageDto();
            storageDto.setIdFile(imagen.getIdArchivo());
            storageDto.setNameFile(imagen.getNombreArchivo());
            storageDto.setIdEntity(imagen.getIdEntidad());

            try {
                String url = storageService.getPresignedUrl(imagen.getIdArchivo());
                storageDto.setKeyR2(url);
            } catch (Exception e) {
                storageDto.setKeyR2("Error al generar URL");
            }

            return storageDto;
        });

        // Convertir rol
        RolDto rolDto = null;
        if (usuario.getRol() != null) {
            rolDto = convertToDto(usuario.getRol());
        }

        // Armar el DTO completo
        UserCompleteDto dto = new UserCompleteDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setDateRecord(usuario.getFechaRegistro());
        dto.setState(usuario.isEstado());
        dto.setUserName(Optional.ofNullable(usuario.getUserName()).orElse(""));
        dto.setFirstName(usuario.getPrimerNombre());
        dto.setMiddleName(Optional.ofNullable(usuario.getSegundoNombre()).orElse(""));
        dto.setFirstSurname(usuario.getPrimerApellido());
        dto.setSecondSurname(Optional.ofNullable(usuario.getSegundoApellido()).orElse(""));
        dto.setAge(Optional.ofNullable(usuario.getEdad()).orElse(""));
        dto.setPhone(Optional.ofNullable(usuario.getCelular()).orElse(""));
        dto.setEmail(usuario.getEmail());
        dto.setPassword(""); // Nunca retornar contraseña
        dto.setRol(rolDto);
        dto.setImages(imagenDto);

        return Optional.of(dto);
    }


    @Override
    public boolean existsById(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }

    @Override
    public Optional<UserDto> getByIdUsuario(Long idUsuario) {
        UsuarioEntity user = usuarioRepository.getByIdUsuario(idUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado!"));

        return Optional.of(convertToDto(user));
    }

    @Override
    public UserDto register(RegisterDto registerDto) {
        if (usuarioRepository.getExistsByEmail(registerDto.getEmail())) {
            throw new ConflictException("El usuario ya existe! con el E-mail: " + registerDto.getEmail());
        } else if (customerRepository.getExistsByEmail(registerDto.getEmail())) {
            throw new ConflictException("El cliente ya existe! con el E-mail: " + registerDto.getEmail());
        }

        UsuarioEntity user = new UsuarioEntity();
        user.setEstado(true);
        user.setUserName("");
        user.setPrimerNombre(registerDto.getFirstName());
        user.setSegundoNombre("");
        user.setPrimerApellido(registerDto.getFirstSurname());
        user.setSegundoApellido("");
        user.setEdad("");
        user.setCelular("");
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));


        RolesEntity rol = rolesRepository.getByName("RESTRINGIDO").orElseGet(() -> {
            RolesEntity rolEntity = new RolesEntity();
            rolEntity.setName("RESTRINGIDO");
            rolEntity.setDescription("Usuario general");

            PermisosEntity permisos = crearPermisosPorDefecto();
            PermisosEntity savedPermisos = permisosRepository.save(permisos);

            rolEntity.setPermisos(savedPermisos);
            return rolesRepository.save(rolEntity);
        });

        user.setRol(rol);
        usuarioRepository.save(user);

        return convertToDto(user);
    }

    @Override
    public UserCompleteDto editUser(UserDto userDto, MultipartFile file, Long idUsuario) {
        try {
            UsuarioEntity user = usuarioRepository.getByIdUsuario(idUsuario)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

            if (userDto != null) {
                Optional.ofNullable(userDto.getUserName()).ifPresent(user::setUserName);
                Optional.ofNullable(userDto.getFirstName()).ifPresent(user::setPrimerNombre);
                Optional.ofNullable(userDto.getMiddleName()).ifPresent(user::setSegundoNombre);
                Optional.ofNullable(userDto.getFirstSurname()).ifPresent(user::setPrimerApellido);
                Optional.ofNullable(userDto.getSecondSurname()).ifPresent(user::setSegundoApellido);
                Optional.ofNullable(userDto.getPhone()).ifPresent(user::setCelular);
                Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
                Optional.ofNullable(userDto.getAge()).ifPresent(user::setEdad);

                // Estado: solo actualizar si se envía explícitamente
                if (userDto.getState() != null) {
                    user.setEstado(userDto.getState());
                }

                // Contraseña: solo actualizar si viene no nula y no vacía
                if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                    if (user.getPassword() == null || !passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    }
                }

                Optional.ofNullable(userDto.getRol()).ifPresent(user::setRol);
            }

            usuarioRepository.save(user);

            // Procesar imagen si se envía
            if (file != null && !file.isEmpty()) {
                Optional<AlmacenamientoEntity> optionalImagen =
                        almacenamientoRepository.findByIdEntidadAndTipoEntidad(user.getIdUsuario(), "usuario");

                if (optionalImagen.isPresent()) {
                    storageService.updateFile(optionalImagen.get().getIdArchivo(), file, "imagenes");
                } else {
                    StorageDto dto = new StorageDto();
                    dto.setIdEntity(user.getIdUsuario());
                    storageService.saveFile(file, dto, "usuario", "imagenes");
                }
            }

            return getUserWithImages(user.getIdUsuario())
                    .orElseThrow(() -> new NotFoundException("No se pudo obtener el usuario actualizado"));

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }



    @Override
    public JwtResponseDto login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtTokenFactory.generateTokenFromDetails(userDetails);

            return new JwtResponseDto(token);
        } catch (AuthenticationException e) {
            throw new JwtAuthenticationException("Credenciales inválidas");
        }
    }


    @Override
    public List<UserDto> getAll() {
        List<UsuarioEntity> usuarios = usuarioRepository.getAll();
        return usuarios.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long idUsuario) throws IOException {

        Optional<UsuarioEntity> userOpt = usuarioRepository.getByIdUsuario(idUsuario);

        if (!userOpt.isPresent()) {
            throw new NotFoundException("Usuario no encontrado por ID: " + idUsuario);
        }

        // Intentar eliminar la imagen asociada, pero no detener el proceso si falla
        try {
            storageService.deleteFileByEntity(idUsuario, "usuario");
        } catch (Exception e) {
            // Puedes registrar el error si lo deseas, pero no interrumpir la ejecución
            System.err.println("No se pudo eliminar la imagen asociada al usuario: " + e.getMessage());
        }

        UsuarioEntity user = userOpt.get();

        // Inhabilitar el usuario aunque haya fallado la eliminación de la imagen
        user.setEstado(false);

        usuarioRepository.save(user);
    }


    private UserDto convertToDto(UsuarioEntity user) {
        UserDto userDto = new UserDto();
        userDto.setIdUsuario(user.getIdUsuario());
        userDto.setDateRecord(user.getFechaRegistro());
        userDto.setState(user.isEstado());
        userDto.setUserName(user.getUserName());
        userDto.setFirstName(user.getPrimerNombre());
        userDto.setMiddleName(user.getSegundoNombre());
        userDto.setFirstSurname(user.getPrimerApellido());
        userDto.setSecondSurname(user.getSegundoApellido());
        userDto.setAge(user.getEdad());
        userDto.setPhone(user.getCelular());
        userDto.setEmail(user.getEmail());
        userDto.setPassword("");
        userDto.setRol(user.getRol());
        return userDto;
    }

    private RolDto convertToDto(RolesEntity rol) {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(rol.getIdRol());
        rolDto.setName(rol.getName());
        rolDto.setDescription(rol.getDescription());
        rolDto.setPermisos(convertToPermissionsDto(rol.getPermisos()));
        return rolDto;
    }

    private RolesEntity convertToEntity(RolDto rolDto) {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRol(rolDto.getIdRol());
        rolesEntity.setName(rolDto.getName());
        rolesEntity.setDescription(rolDto.getDescription());
        rolesEntity.setPermisos(convertToPermisosEntity(rolDto.getPermisos()));
        return rolesEntity;
    }

    private PermisosEntity crearPermisosPorDefecto() {
        PermisosEntity permisos = new PermisosEntity();
        permisos.setEditPermisos(false);
        permisos.setVistaUsuarios(true);
        permisos.setEditUsuarios(false);
        permisos.setVistaProyectos(true);
        permisos.setEditProyectos(true);
        permisos.setVistaDisenios3d(true);
        permisos.setEditDisenios3d(true);
        permisos.setVistaMateriales(true);
        permisos.setEditMateriales(false);
        permisos.setVistaInformes(false);
        permisos.setVistaCategorias(true);
        permisos.setEditCategorias(false);
        return permisos;
    }

    private PermissionsDto convertToPermissionsDto (PermisosEntity permisos) {
        PermissionsDto dto = new PermissionsDto();
        dto.setIdPermiso(permisos.getIdPermiso());
        dto.setEditPermisos(permisos.isEditPermisos());
        dto.setVistaUsuarios(permisos.isVistaUsuarios());
        dto.setEditUsuarios(permisos.isEditUsuarios());
        dto.setVistaProyectos(permisos.isVistaProyectos());
        dto.setEditProyectos(permisos.isEditProyectos());
        dto.setVistaDisenios3d(permisos.isEditPermisos());
        dto.setEditDisenios3d(permisos.isEditDisenios3d());
        dto.setVistaMateriales(permisos.isVistaMateriales());
        dto.setEditMateriales(permisos.isEditMateriales());
        dto.setVistaInformes(permisos.isEditPermisos());
        dto.setVistaCategorias(permisos.isEditPermisos());
        dto.setEditCategorias(permisos.isEditCategorias());

        return dto;
    }

    private PermisosEntity convertToPermisosEntity(PermissionsDto dto) {
        PermisosEntity permisos = new PermisosEntity();
        permisos.setEditPermisos(dto.isEditPermisos());
        permisos.setVistaUsuarios(dto.isVistaUsuarios());
        permisos.setEditUsuarios(dto.isEditUsuarios());
        permisos.setVistaProyectos(dto.isVistaProyectos());
        permisos.setEditProyectos(dto.isEditProyectos());
        permisos.setVistaDisenios3d(dto.isVistaDisenios3d());
        permisos.setEditDisenios3d(dto.isEditDisenios3d());
        permisos.setVistaMateriales(dto.isVistaMateriales());
        permisos.setEditMateriales(dto.isEditMateriales());
        permisos.setVistaInformes(dto.isVistaInformes());
        permisos.setVistaCategorias(dto.isVistaCategorias());
        permisos.setEditCategorias(dto.isEditCategorias());

        return permisos;
    }
}

