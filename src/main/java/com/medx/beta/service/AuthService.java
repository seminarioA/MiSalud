package com.medx.beta.service;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegistroRequest;
import com.medx.beta.model.Usuario;

public interface AuthService {

    Usuario registrarUsuario(RegistroRequest registroRequest);

    AuthResponse autenticar(LoginRequest loginRequest);

    AuthUserResponse obtenerUsuarioActual();
}