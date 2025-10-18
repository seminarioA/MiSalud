package com.medx.beta.service;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegistroRequest;

public interface AuthService {
    
    Usuario registrarUsuario(RegistroRequest registroRequest);
    
    AuthResponse autenticar(LoginRequest loginRequest);
    
    Usuario obtenerUsuarioActual();
}