package com.medx.beta.service;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.AuthUserResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegisterPatientRequest;
import com.medx.beta.dto.RegisterPatientResponse;

public interface AuthService {

    AuthResponse autenticar(LoginRequest request);

    AuthUserResponse usuarioActual();

    RegisterPatientResponse registrarPaciente(RegisterPatientRequest request);
}
