package com.envifo_backend_java.Envifo_backend_java.infrastructure.security;

import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.StorageDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.CustomerCompleteDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.service.StorageService;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.AlmacenamientoRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.UsuarioRepository;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.repository.ClientesRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private final UsuarioRepository usuarioRepository;
    private final ClientesRepository clientesRepository;
    private final AlmacenamientoRepository almacenamientoRepository;
    private final StorageService storageService;

    @Autowired
    public JwtTokenFactory(
            UsuarioRepository usuarioRepository,
            ClientesRepository clientesRepository,
            AlmacenamientoRepository almacenamientoRepository,
            StorageService storageService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.clientesRepository = clientesRepository;
        this.almacenamientoRepository = almacenamientoRepository;
        this.storageService = storageService;
    }

    public String generateTokenFromDetails(CustomUserDetails userDetails) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        Map<String, Object> claims = new HashMap<>();

        if (userDetails.esUsuario()) {
            Long idUsuario = userDetails.getUsuario().getIdUsuario();
            Optional<UserCompleteDto> userOpt = getUserWithImages(idUsuario);

            if (userOpt.isEmpty()) {
                throw new RuntimeException("No se pudo generar token: usuario no encontrado.");
            }

            UserCompleteDto dto = userOpt.get();

            claims.put("tipo", "USUARIO");
            claims.put("idUsuario", dto.getIdUsuario());
            claims.put("email", dto.getEmail());
            claims.put("userName", dto.getUserName());
            claims.put("primerNombre", dto.getFirstName());
            claims.put("segundoNombre", dto.getMiddleName());
            claims.put("primerApellido", dto.getFirstSurname());
            claims.put("segundoApellido", dto.getSecondSurname());
            claims.put("edad", dto.getAge());
            claims.put("celular", dto.getPhone());

            if (dto.getRol() != null) {
                claims.put("idRol", dto.getRol().getIdRol());
                claims.put("rol", dto.getRol().getName());
                claims.put("Permisos", dto.getRol().getPermisos());
            }

            dto.getImages().ifPresent(img -> claims.put("imagen", img));

        } else {
            Long idCliente = userDetails.getCliente().getIdCliente();
            Optional<CustomerCompleteDto> customerOpt = getCustomerWithImages(idCliente);

            if (customerOpt.isEmpty()) {
                throw new RuntimeException("No se pudo generar token: cliente no encontrado.");
            }

            CustomerCompleteDto dto = customerOpt.get();

            claims.put("tipo", "EMPRESA");
            claims.put("idCliente", dto.getCustomerId());
            claims.put("email", dto.getEmail());
            claims.put("nombre", dto.getName());
            claims.put("telefono", dto.getPhone());
            claims.put("direccion", dto.getAddress());
            claims.put("url", dto.getUrl());

            if (dto.getRolCustomer() != null) {
                claims.put("idRol", dto.getRolCustomer().getIdRol());
                claims.put("rol", dto.getRolCustomer().getName());
                claims.put("Permisos", dto.getRolCustomer().getPermisos());
            }

            dto.getImages().ifPresent(img -> claims.put("imagen", img));
        }

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(getKey())
                .compact();
    }

    private Optional<UserCompleteDto> getUserWithImages(Long idUsuario) {
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.getByIdUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        UsuarioEntity usuario = usuarioOpt.get();
        Optional<StorageDto> imageDto = buildImageDto(idUsuario, "usuario");

        RolDto rolDto = null;
        if (usuario.getRol() != null) {
            rolDto = convertToDto(usuario.getRol());
        }

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
        dto.setPassword("");
        dto.setRol(rolDto);
        dto.setImages(imageDto);

        return Optional.of(dto);
    }

    private Optional<CustomerCompleteDto> getCustomerWithImages(Long idCliente) {
        Optional<ClientesEntity> clienteOpt = clientesRepository.getByIdCliente(idCliente);
        if (clienteOpt.isEmpty()) {
            return Optional.empty();
        }

        ClientesEntity cliente = clienteOpt.get();
        Optional<StorageDto> imageDto = buildImageDto(idCliente, "cliente");

        RolDto rolDto = null;
        if (cliente.getRol() != null) {
            rolDto = convertToDto(cliente.getRol());
        }

        CustomerCompleteDto dto = new CustomerCompleteDto();
        dto.setCustomerId(cliente.getIdCliente());
        dto.setEmail(cliente.getEmail());
        dto.setName(cliente.getNombre());
        dto.setAddress(cliente.getDireccion());
        dto.setPhone(cliente.getTelefono());
        dto.setPassword("");
        dto.setUrl(Optional.ofNullable(cliente.getUrl()).orElse(""));
        dto.setStateCustomer(cliente.isEstado());
        dto.setRegistrationDate(cliente.getFechaRegistro());
        dto.setRolCustomer(rolDto);
        dto.setImages(imageDto);

        return Optional.of(dto);
    }

    private Optional<StorageDto> buildImageDto(Long idEntidad, String tipoEntidad) {
        return almacenamientoRepository.findByIdEntidadAndTipoEntidad(idEntidad, tipoEntidad).map(entity -> {
            StorageDto dto = new StorageDto();
            dto.setIdFile(entity.getIdArchivo());
            dto.setNameFile(entity.getNombreArchivo());
            dto.setIdEntity(entity.getIdEntidad());
            try {
                dto.setKeyR2(storageService.getPresignedUrl(entity.getIdArchivo()));
            } catch (Exception e) {
                dto.setKeyR2("Error al generar URL");
            }
            return dto;
        });
    }

    private RolDto convertToDto(RolesEntity rol) {
        RolDto rolDto = new RolDto();
        rolDto.setIdRol(rol.getIdRol());
        rolDto.setName(rol.getName());
        rolDto.setDescription(rol.getDescription());
        rolDto.setPermisos(convertToPermissionsDto(rol.getPermisos()));
        return rolDto;
    }

    private PermissionsDto convertToPermissionsDto(PermisosEntity permisos) {
        PermissionsDto dto = new PermissionsDto();
        dto.setIdPermiso(permisos.getIdPermiso());
        dto.setEditPermisos(permisos.isEditPermisos());
        dto.setVistaUsuarios(permisos.isVistaUsuarios());
        dto.setEditUsuarios(permisos.isEditUsuarios());
        dto.setVistaProyectos(permisos.isVistaProyectos());
        dto.setEditProyectos(permisos.isEditProyectos());
        dto.setVistaDisenios3d(permisos.isVistaDisenios3d());
        dto.setEditDisenios3d(permisos.isEditDisenios3d());
        dto.setVistaMateriales(permisos.isVistaMateriales());
        dto.setEditMateriales(permisos.isEditMateriales());
        dto.setVistaInformes(permisos.isVistaInformes());
        dto.setVistaCategorias(permisos.isVistaCategorias());
        dto.setEditCategorias(permisos.isEditCategorias());
        return dto;
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
