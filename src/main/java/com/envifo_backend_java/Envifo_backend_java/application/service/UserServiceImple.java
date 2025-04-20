package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import com.envifo_backend_java.Envifo_backend_java.domain.service.UserService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.JwtAuthenticationException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.PermisosRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.RolesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImple implements UserService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolServiceImple rolService;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PermisosRepository permisosRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDto> getByIdUsuario(Long idUsuario) {
        // Obtener el usuario desde la base de datos
        UsuarioEntity user = usuarioRepository.getByIdUsuario(idUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado!"));

        UserDto userDto = new UserDto();
        userDto.setIdUsuario(user.getIdUsuario());
        userDto.setDateRecord(user.getFechaRegistro());
        userDto.setState(user.isEstado());
        userDto.setUserName(user.getUserName());
        userDto.setName(user.getNombre());
        userDto.setLastName(user.getApellido());
        userDto.setAge(user.getEdad());
        userDto.setPhone(user.getCelular());
        userDto.setEmail(user.getEmail());
        userDto.setPassword("");  // No devolver la contraseña por seguridad
        userDto.setBirthDate(user.getFechaNacimiento());
        userDto.setRol(user.getRol());

        return Optional.of(userDto);
    }


    @Override
    public UserDto register(RegisterDto registerDto) {
        if (usuarioRepository.getExistsByEmail(registerDto.getEmail())) {
            throw new ConflictException("El usuario ya existe!");
        }

        // Convertir RegisterDto a UsuarioEntity
        UsuarioEntity user = new UsuarioEntity();
        user.setEstado(registerDto.isState());
        user.setUserName(registerDto.getUserName());
        user.setNombre(registerDto.getName());
        user.setApellido(registerDto.getLastName());
        user.setEdad(registerDto.getAge());
        user.setCelular(registerDto.getPhone());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFechaNacimiento(registerDto.getBirthDate());

        // Verificar si el rol "RESTRINGIDO" ya existe, y si no, crearlo
        RolesEntity rol = rolesRepository.getByName("RESTRINGIDO").orElseGet(() -> {

            RolesEntity rolEntity = new RolesEntity();
            rolEntity.setName("RESTRINGIDO");
            rolEntity.setDescription("Usuario general");

            // Crear permisos predeterminados
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

            // Guardar permisos en la base de datos
            PermisosEntity savedPermisos = permisosRepository.save(permisos);

            // Asignar permisos al rol
            rolEntity.setPermisos(savedPermisos);

            // Guardar el nuevo rol en la base de datos
            return rolesRepository.save(rolEntity);
        });

        // Asignar el rol al usuario
        user.setRol(rol);

        // Guardar el usuario
        usuarioRepository.save(user);

        // Convertir UsuarioEntity a UserDto antes de retornarlo
        UserDto userDto = new UserDto();
        userDto.setState(user.isEstado());
        userDto.setUserName(user.getUserName());
        userDto.setName(user.getNombre());
        userDto.setLastName(user.getApellido());
        userDto.setAge(user.getEdad());
        userDto.setPhone(user.getCelular());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setBirthDate(user.getFechaNacimiento());

        // Convertir RolDto a RolesEntity
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRol(rol.getIdRol());
        rolesEntity.setName(rol.getName());
        rolesEntity.setDescription(rol.getDescription());
        rolesEntity.setPermisos(rol.getPermisos());

        // Convertir PermissionsDto a PermisosEntity
        PermisosEntity permisosEntity = new PermisosEntity();
        permisosEntity.setEditPermisos(rol.getPermisos().isEditPermisos());
        permisosEntity.setVistaUsuarios(rol.getPermisos().isVistaUsuarios());
        permisosEntity.setEditUsuarios(rol.getPermisos().isEditUsuarios());
        permisosEntity.setVistaProyectos(rol.getPermisos().isVistaProyectos());
        permisosEntity.setEditProyectos(rol.getPermisos().isEditProyectos());
        permisosEntity.setVistaDisenios3d(rol.getPermisos().isVistaDisenios3d());
        permisosEntity.setEditDisenios3d(rol.getPermisos().isEditDisenios3d());
        permisosEntity.setVistaMateriales(rol.getPermisos().isVistaMateriales());
        permisosEntity.setEditMateriales(rol.getPermisos().isEditMateriales());
        permisosEntity.setVistaInformes(rol.getPermisos().isVistaInformes());
        permisosEntity.setVistaCategorias(rol.getPermisos().isVistaCategorias());
        permisosEntity.setEditCategorias(rol.getPermisos().isEditCategorias());

        // Asignar permisos al rol
        rolesEntity.setPermisos(permisosEntity);
        userDto.setRol(rolesEntity);

        return userDto;
    }

    @Override
    public void editUser(UserDto userDto) {
        try {
            UsuarioEntity user = usuarioRepository.getByIdUsuario(userDto.getIdUsuario())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

            Optional.ofNullable(userDto.getUserName()).ifPresent(user::setUserName);
            Optional.ofNullable(userDto.getName()).ifPresent(user::setNombre);
            Optional.ofNullable(userDto.getLastName()).ifPresent(user::setApellido);
            Optional.ofNullable(userDto.getPhone()).ifPresent(user::setCelular);
            Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
            Optional.ofNullable(userDto.getBirthDate()).ifPresent(user::setFechaNacimiento);
            Optional.ofNullable(userDto.getAge()).filter(age -> age > 0).ifPresent(user::setEdad);
            Optional.ofNullable(userDto.isState()).filter(state -> state != user.isEstado()).ifPresent(user::setEstado);

            Optional.ofNullable(userDto.getPassword())
                    .filter(pass -> user.getPassword() != null && !passwordEncoder.matches(pass, user.getPassword()))
                    .map(passwordEncoder::encode)
                    .ifPresent(user::setPassword);

            if (userDto.getRol() != null) {

                RolDto newRol = convertToDto(userDto.getRol());
                RolDto rolDto = rolService.createRol(newRol);// Método de conversión
                RolesEntity rolesEntity = convertToEntity(rolDto);

                user.setRol(rolesEntity);
            }

            usuarioRepository.save(user);
        } catch (NotFoundException e) {
            throw e;  // Relanzar la excepción específica si el usuario no se encuentra
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
            String token = jwtGenerator.generateToken(authentication);
            return new JwtResponseDto(token);
        } catch (AuthenticationException e) {
            throw new JwtAuthenticationException("Credenciales inválidas");
        }
    }

    @Override
    public UserDto getLoguedUser(HttpHeaders headers) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = ((User) authentication.getPrincipal()).getUsername();

        UsuarioEntity user = usuarioRepository.getByEmail(email)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        UserDto userDto = new UserDto();
        userDto.setIdUsuario(user.getIdUsuario());
        userDto.setDateRecord(user.getFechaRegistro());
        userDto.setState(user.isEstado());
        userDto.setUserName(user.getUserName());
        userDto.setName(user.getNombre());
        userDto.setLastName(user.getApellido());
        userDto.setAge(user.getEdad());
        userDto.setPhone(user.getCelular());
        userDto.setEmail(user.getEmail());
        userDto.setBirthDate(user.getFechaNacimiento());
        userDto.setRol(user.getRol());
        return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        // Obtener la lista de entidades desde el repositorio
        List<UsuarioEntity> usuarios = usuarioRepository.getAll();

        // Convertir UsuarioEntity a UserDto
        return usuarios.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setIdUsuario(user.getIdUsuario());
            userDto.setDateRecord(user.getFechaRegistro());
            userDto.setState(user.isEstado());
            userDto.setUserName(user.getUserName());
            userDto.setName(user.getNombre());
            userDto.setLastName(user.getApellido());
            userDto.setAge(user.getEdad());
            userDto.setPhone(user.getCelular());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(""); // No devolver la contraseña por seguridad
            userDto.setBirthDate(user.getFechaNacimiento());
            userDto.setRol(user.getRol());

            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long idUsuario) {
        usuarioRepository.delete(idUsuario);
    }


    private RolDto convertToDto(RolesEntity entity) {
        RolDto dto = new RolDto();
        dto.setIdRol(entity.getIdRol());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPermisos(entity.getPermisos());
        return dto;
    }

    private RolesEntity convertToEntity(RolDto rolDto) {
        RolesEntity entity = new RolesEntity();
        entity.setIdRol(rolDto.getIdRol());
        entity.setName(rolDto.getName());
        entity.setDescription(rolDto.getDescription());
        return entity;
    }


}