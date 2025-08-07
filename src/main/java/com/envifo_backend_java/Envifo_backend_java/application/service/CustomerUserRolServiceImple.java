package com.envifo_backend_java.Envifo_backend_java.application.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.PermissionsDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.*;
import com.envifo_backend_java.Envifo_backend_java.domain.repository.*;
import com.envifo_backend_java.Envifo_backend_java.domain.service.CustomerUserRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerUserRolServiceImple implements CustomerUserRolService {

    private final CustomerUserRolRepository repository;

    private final UserRepository userRepository;

    private final RolRepository rolRepository;

    private final CustomerRepository customerRepository;

    private final RolServiceImple rolServiceImple;

    private final PermissionsRepository permissionsRepository;

    @Autowired
    public CustomerUserRolServiceImple(CustomerUserRolRepository repository, UserRepository userRepository, RolRepository rolRepository, CustomerRepository customerRepository, RolServiceImple rolServiceImple, PermissionsRepository permissionsRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.customerRepository = customerRepository;
        this.rolServiceImple = rolServiceImple;
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public void assignRolToUser(Long idUsuario, Long idCliente, RolDto rolDto) {
        UsuarioEntity usuario = userRepository.getByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ClientesEntity cliente = customerRepository.getByIdCliente(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        RolDto newRol = rolServiceImple.createRol(rolDto);

        RolesEntity rol = rolRepository.getByIdRol(newRol.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Verifica si ya existe asignación para evitar duplicados
        repository.getByUsuarioIdUsuarioAndClienteIdCliente(idUsuario, idCliente)
                .ifPresent(existing -> {
                    throw new RuntimeException("El usuario ya tiene un rol asignado para este cliente");
                });

        ClienteUsuarioRolEntity entity = new ClienteUsuarioRolEntity();
        entity.setUsuario(usuario);
        entity.setCliente(cliente);
        entity.setRol(rol);

        repository.saveClienteUsuarioRolEntity(entity);
    }

    @Override
    public Optional<UserDto> getUserRolIntoCustomer(Long idUsuario, Long idCliente) {

        Optional<ClienteUsuarioRolEntity> userWithRol =
                repository.getByUsuarioIdUsuarioAndClienteIdCliente(idUsuario, idCliente);

        Optional<UserDto> userDto = Optional.of(convertToUserDto(userWithRol.get()));

        return userDto;
    }


    @Override
    public List<UserDto> getRolByCustomer(Long idCliente) {

        List<ClienteUsuarioRolEntity> usersWithRoles =
                repository.getByClienteIdCliente(idCliente);

        List<UserDto> usersWithRolesDto = new ArrayList<>();

        for (ClienteUsuarioRolEntity user : usersWithRoles) {
            UserDto dto = convertToUserDto(user);
            usersWithRolesDto.add(dto);
        }

        return usersWithRolesDto;
    }

    @Override
    public void deleteAssignment(Long idAsignacion) {
        Optional<ClienteUsuarioRolEntity> assignmentOpt = repository.getByIdCliUsuRol(idAsignacion);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Asignación no encontrada con id: " + idAsignacion);
        }

        ClienteUsuarioRolEntity assignment = assignmentOpt.get();

        // Primero eliminamos la asignación
        repository.deleteById(idAsignacion);

        // Luego eliminamos el rol asociado
        rolServiceImple.deleteByIdRol(assignment.getRol().getIdRol());

        // Luego eliminamos el permiso asociado a rol
        permissionsRepository.deleteByIdPermiso(assignment.getRol().getPermisos().getIdPermiso());
    }


    @Override
    public void updateUserRolIntoCustomer(Long idUsuario, Long idCliente, RolDto rolDto) {
        UsuarioEntity usuario = userRepository.getByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ClientesEntity cliente = customerRepository.getByIdCliente(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ClienteUsuarioRolEntity entity = repository.getByUsuarioIdUsuarioAndClienteIdCliente(idUsuario, idCliente)
                .orElseThrow(() -> new RuntimeException("La asignación usuario-cliente no existe"));

        // Si el rol no existe aún, lo crea
        RolDto newRol = rolServiceImple.editRol(rolDto);

        RolesEntity rol = rolRepository.getByIdRol(newRol.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        entity.setRol(rol);

        repository.saveClienteUsuarioRolEntity(entity);
    }

    private UserDto convertToUserDto (ClienteUsuarioRolEntity userWhitRol) {
        UserDto dto = new UserDto();

        dto.setIdUsuario(userWhitRol.getUsuario().getIdUsuario());
        dto.setDateRecord(userWhitRol.getUsuario().getFechaRegistro());
        dto.setState(userWhitRol.getUsuario().isEstado());
        dto.setUserName(userWhitRol.getUsuario().getUserName());
        dto.setFirstName(userWhitRol.getUsuario().getPrimerNombre());
        dto.setMiddleName(userWhitRol.getUsuario().getSegundoNombre());
        dto.setFirstSurname(userWhitRol.getUsuario().getPrimerApellido());
        dto.setSecondSurname(userWhitRol.getUsuario().getSegundoApellido());
        dto.setAge(userWhitRol.getUsuario().getEdad());
        dto.setPhone(userWhitRol.getUsuario().getCelular());
        dto.setEmail(userWhitRol.getUsuario().getEmail());
        dto.setPassword("");
        dto.setRol(userWhitRol.getRol());

        return dto;
    }

}
