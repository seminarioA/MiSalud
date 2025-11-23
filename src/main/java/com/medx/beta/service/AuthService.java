package com.medx.beta.service;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;

public interface AuthService {

    AuthResponse autenticar(LoginRequest request);

    AuthUserResponse usuarioActual();
}

