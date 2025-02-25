package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.domain.model.JwtResponseDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.LoginDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.RegisterDto;
import com.envifo_backend_java.Envifo_backend_java.domain.model.UserDto;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserDto> getByIdUsuario(Long idUsuario);

    public UserDto register(RegisterDto registerDto);

    void editUser(UserDto userDto);

    public JwtResponseDto login(LoginDto loginDto);

    UserDto getLoguedUser(HttpHeaders headers);

    List<UserDto> getAll();

    void delete(Long idUsuario);

}
