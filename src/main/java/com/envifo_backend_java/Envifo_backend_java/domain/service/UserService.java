package com.envifo_backend_java.Envifo_backend_java.domain.service;

import com.envifo_backend_java.Envifo_backend_java.application.dto.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserCompleteDto> getUserWithImages(Long idUsuario);

    boolean existsById (Long idUsuario);

    Optional<UserDto> getByIdUsuario(Long idUsuario);

    UserDto register(RegisterDto registerDto);

    UserCompleteDto editUser(UserDto userDto, MultipartFile file);

    JwtResponseDto login(LoginDto loginDto);

    List<UserDto> getAll();

    void deleteUser(Long idUsuario) throws IOException;

}
