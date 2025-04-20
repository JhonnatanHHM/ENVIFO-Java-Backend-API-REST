package com.envifo_backend_java.Envifo_backend_java.application.service;


import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.PermisosEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.RolesEntity;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.UsuarioEntity;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.RolesRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("userDetailService")
@Transactional(readOnly=true)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private RolesRepository rolesRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtener el usuario desde la base de datos
        UsuarioEntity user = usuarioRepository.getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado!"));

        // Obtener el rol único del usuario
        RolesEntity rol = user.getRol();

        // Obtener los permisos del usuario
        PermisosEntity permisos = rol.getPermisos();

        // Crear la lista de GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Añadir el rol como una autoridad
        authorities.add(new SimpleGrantedAuthority("ROL_" + rol.getName()));

        // Incluir el ID del permiso como una autoridad
        authorities.add(new SimpleGrantedAuthority("ID_PERMISOS_" + permisos.getIdPermiso()));

        // Mapear los permisos booleanos a sus nombres de autoridad
        Map<String, Boolean> permisosMap = new HashMap<>();
        permisosMap.put("EDIT_PERMISOS", permisos.isEditPermisos());
        permisosMap.put("VISTA_USUARIOS", permisos.isVistaUsuarios());
        permisosMap.put("EDIT_USUARIOS", permisos.isEditUsuarios());
        permisosMap.put("VISTA_PROYECTOS", permisos.isVistaProyectos());
        permisosMap.put("EDIT_PROYECTOS", permisos.isEditProyectos());
        permisosMap.put("VISTA_DISENIOS_3D", permisos.isVistaDisenios3d());
        permisosMap.put("EDIT_DISENIOS_3D", permisos.isEditDisenios3d());
        permisosMap.put("VISTA_MATERIALES", permisos.isVistaMateriales());
        permisosMap.put("EDIT_MATERIALES", permisos.isEditMateriales());
        permisosMap.put("VISTA_INFORMES", permisos.isVistaInformes());
        permisosMap.put("VISTA_CATEGORIAS", permisos.isVistaCategorias());
        permisosMap.put("EDIT_CATEGORIAS", permisos.isEditCategorias());

        // Añadir las autoridades dinámicamente
        permisosMap.forEach((authority, hasPermission) -> {
            if (hasPermission) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        });

        // Retornar el objeto User con email, contraseña y autoridades
        return new User(user.getEmail(), user.getPassword(), authorities);
    }

}