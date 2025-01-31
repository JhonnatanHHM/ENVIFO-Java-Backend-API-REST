package com.envifo_backend_java.Envifo_backend_java.application.service.interfaces;

import com.envifo_backend_java.Envifo_backend_java.domain.model.JwtResponseDom;
import com.envifo_backend_java.Envifo_backend_java.domain.model.LoginDom;
import com.envifo_backend_java.Envifo_backend_java.domain.model.RegisterDom;
import com.envifo_backend_java.Envifo_backend_java.domain.model.UserDom;
import com.envifo_backend_java.Envifo_backend_java.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserDom> getByIdUsuario(Long idUsuario);

    public UserDom register(RegisterDom registerDto);

    void editUser(UserDom userDom);

    public JwtResponseDom login(LoginDom loginDto);

    UserDom getLoguedUser(HttpHeaders headers);

    List<UserDom> getAll();

    void delete(Long idUsuario);

}
