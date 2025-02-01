package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.RolService;
import com.envifo_backend_java.Envifo_backend_java.application.service.interfaces.UserService;
import com.envifo_backend_java.Envifo_backend_java.domain.model.*;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.ConflictException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.JwtAuthenticationException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.exceptions.NotFoundException;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.PermisosRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.RolesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.security.JwtGenerator;
import org.springframework.beans.BeanUtils;
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
    private RolService rolService;
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
    public Optional<UserDom> getByIdUsuario(Long idUsuario) {
        // Obtener el usuario desde la base de datos
        UsuarioEntity user = usuarioRepository.getByIdUsuario(idUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado!"));

        UserDom userDom = new UserDom();
        userDom.setIdUsuario(user.getIdUsuario());
        userDom.setDateRecord(user.getFechaRegistro());
        userDom.setState(user.isEstado());
        userDom.setUserName(user.getUserName());
        userDom.setName(user.getNombre());
        userDom.setLastName(user.getApellido());
        userDom.setAge(user.getEdad());
        userDom.setPhone(user.getCelular());
        userDom.setEmail(user.getEmail());
        userDom.setPassword("");  // No devolver la contraseña por seguridad
        userDom.setBirthDate(user.getFechaNacimiento());
        userDom.setRol(user.getRol());

        return Optional.of(userDom);
    }



    @Override
    public UserDom register(RegisterDom registerDom) {
        if (usuarioRepository.getExistsByEmail(registerDom.getEmail())) {
            throw new ConflictException("El usuario ya existe!");
        }

        // Convertir RegisterDom a UsuarioEntity
        UsuarioEntity user = new UsuarioEntity();
        user.setEstado(registerDom.isState());
        user.setUserName(registerDom.getUserName());
        user.setNombre(registerDom.getName());
        user.setApellido(registerDom.getLastName());
        user.setEdad(registerDom.getAge());
        user.setCelular(registerDom.getPhone());
        user.setEmail(registerDom.getEmail());
        user.setPassword(passwordEncoder.encode(registerDom.getPassword()));
        user.setFechaNacimiento(registerDom.getBirthDate());

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

        // Convertir UsuarioEntity a UserDom antes de retornarlo
        UserDom userDom = new UserDom();
        userDom.setState(user.isEstado());
        userDom.setUserName(user.getUserName());
        userDom.setName(user.getNombre());
        userDom.setLastName(user.getApellido());
        userDom.setAge(user.getEdad());
        userDom.setPhone(user.getCelular());
        userDom.setEmail(user.getEmail());
        userDom.setPassword(user.getPassword());
        userDom.setBirthDate(user.getFechaNacimiento());

        // Convertir RolDom a RolesEntity
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRol(rol.getIdRol());
        rolesEntity.setName(rol.getName());
        rolesEntity.setDescription(rol.getDescription());
        rolesEntity.setPermisos(rol.getPermisos());

        // Convertir PermissionsDom a PermisosEntity
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
        userDom.setRol(rolesEntity);

        return userDom;
    }



    @Override
    public void editUser(UserDom userDom) {
        try {
            // Buscar usuario existente por ID
            UsuarioEntity user = usuarioRepository.getByIdUsuario(userDom.getIdUsuario())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

            // Actualizar campos con valores no nulos
            Optional.ofNullable(userDom.getUserName()).ifPresent(user::setUserName);
            Optional.ofNullable(userDom.getName()).ifPresent(user::setNombre);
            Optional.ofNullable(userDom.getLastName()).ifPresent(user::setApellido);
            Optional.ofNullable(userDom.getPhone()).ifPresent(user::setCelular);
            Optional.ofNullable(userDom.getEmail()).ifPresent(user::setEmail);
            Optional.ofNullable(userDom.getBirthDate()).ifPresent(user::setFechaNacimiento);
            Optional.of(userDom.getAge()).filter(age -> age > 0).ifPresent(user::setEdad);
            Optional.of(userDom.isState()).filter(state -> state != user.isEstado()).ifPresent(user::setEstado);

            // Actualizar contraseña si es diferente
            Optional.ofNullable(userDom.getPassword())
                    .filter(pass -> !passwordEncoder.matches(pass, user.getPassword()))
                    .map(passwordEncoder::encode)
                    .ifPresent(user::setPassword);

            // Buscar rol existente o crearlo
            RolesEntity rolEntity = rolesRepository.getByName(userDom.getRol().getName()).orElseGet(() -> {
                RolesEntity newRol = new RolesEntity();
                newRol.setName(userDom.getRol().getName());
                newRol.setDescription(userDom.getRol().getDescription());
                return rolesRepository.save(newRol);
            });

            // Obtener permisos actuales del rol
            PermisosEntity permisosEntity = rolEntity.getPermisos();

            // Si el rol es "RESTRINGIDO", no se pueden modificar los permisos
            if ("RESTRINGIDO".equalsIgnoreCase(rolEntity.getName()) && permisosEntity != null) {
                throw new RuntimeException("No se pueden modificar los permisos del rol RESTRINGIDO");
            }

            // Verificar si se está intentando reutilizar un idPermiso que ya pertenece a un rol "RESTRINGIDO"
            if (userDom.getRol().getPermisos() != null && userDom.getRol().getPermisos()
                    .getIdPermiso() != null) {
                Long idPermiso = userDom.getRol().getPermisos().getIdPermiso();

                // Buscar si este `idPermiso` ya está vinculado a un rol "RESTRINGIDO"
                boolean permisoRestringido = rolesRepository.existsByPermisos_IdPermisoAndNameIgnoreCase(idPermiso, "RESTRINGIDO") ||
                        rolesRepository.existsByPermisos_IdPermisoAndNameIgnoreCase(idPermiso, "GLOBAL");

                if (permisoRestringido) {
                    throw new RuntimeException("No se puede reutilizar un idPermiso asociado al rol GLOBAL o RESTRINGIDO");
                }
            }

            // Verificar si se debe crear una nueva lista de permisos
            if (userDom.getRol().getPermisos() != null &&
                    (userDom.getRol().getPermisos().getIdPermiso() == null)) {

                // Si `idPermiso` es nulo, se crean nuevos permisos
                permisosEntity = new PermisosEntity();
                BeanUtils.copyProperties(userDom.getRol().getPermisos(), permisosEntity);
                permisosEntity = permisosRepository.save(permisosEntity);
            } else if (permisosEntity != null) {
                // Si `idPermiso` existe y no es "RESTRINGIDO", actualizar los valores
                BeanUtils.copyProperties(userDom.getRol().getPermisos(), permisosEntity);
                permisosEntity = permisosRepository.save(permisosEntity);
            }

            // Asignar los permisos al rol y guardar cambios
            rolEntity.setPermisos(permisosEntity);
            rolesRepository.save(rolEntity);

            // Asignar el nuevo rol al usuario
            user.setRol(rolEntity);

            // Guardar cambios en la base de datos
            usuarioRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }




    @Override
    public JwtResponseDom login(LoginDom loginDom) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDom.getEmail(),
                            loginDom.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new JwtResponseDom(token);
        } catch (AuthenticationException e) {
            throw new JwtAuthenticationException("Credenciales inválidas");
        }
    }

    @Override
    public UserDom getLoguedUser(HttpHeaders headers) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = ((User) authentication.getPrincipal()).getUsername();

        UsuarioEntity user = usuarioRepository.getByEmail(email)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        UserDom userDom = new UserDom();
        userDom.setIdUsuario(user.getIdUsuario());
        userDom.setDateRecord(user.getFechaRegistro());
        userDom.setState(user.isEstado());
        userDom.setUserName(user.getUserName());
        userDom.setName(user.getNombre());
        userDom.setLastName(user.getApellido());
        userDom.setAge(user.getEdad());
        userDom.setPhone(user.getCelular());
        userDom.setEmail(user.getEmail());
        userDom.setBirthDate(user.getFechaNacimiento());
        userDom.setRol(user.getRol());
        return userDom;
    }

    @Override
    public List<UserDom> getAll() {
        // Obtener la lista de entidades desde el repositorio
        List<UsuarioEntity> usuarios = usuarioRepository.getAll();

        // Convertir UsuarioEntity a UserDom
        return usuarios.stream().map(user -> {
            UserDom userDom = new UserDom();
            userDom.setIdUsuario(user.getIdUsuario());
            userDom.setDateRecord(user.getFechaRegistro());
            userDom.setState(user.isEstado());
            userDom.setUserName(user.getUserName());
            userDom.setName(user.getNombre());
            userDom.setLastName(user.getApellido());
            userDom.setAge(user.getEdad());
            userDom.setPhone(user.getCelular());
            userDom.setEmail(user.getEmail());
            userDom.setPassword(""); // No devolver la contraseña por seguridad
            userDom.setBirthDate(user.getFechaNacimiento());
            userDom.setRol(user.getRol());

            return userDom;
        }).collect(Collectors.toList());
    }


    @Override
    public void delete(Long idUsuario) {
        usuarioRepository.delete(idUsuario);
    }

}