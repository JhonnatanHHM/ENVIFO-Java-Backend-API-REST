package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.RolDto;
import com.envifo_backend_java.Envifo_backend_java.application.dto.UserDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.entity.ClienteUsuarioRolEntity;

import java.util.List;
import java.util.Optional;


public interface CustomerUserRolService {

    void assignRolToUser(Long idUsuario, Long idCliente, RolDto rolDto);
    Optional<UserDto> getUserRolIntoCustomer(Long idUsuario, Long idCliente);
    List<UserDto> getRolByCustomer(Long idCliente);
    void deleteAssignment(Long idAsignacion);
    void updateUserRolIntoCustomer(Long idUsuario, Long idCliente, RolDto rolDto);

}
