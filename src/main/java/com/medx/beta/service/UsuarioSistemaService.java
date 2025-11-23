package com.medx.beta.service;

import com.medx.beta.dto.UsuarioSistemaRequest;
import com.medx.beta.dto.UsuarioSistemaResponse;

import java.util.List;

public interface UsuarioSistemaService {

    List<UsuarioSistemaResponse> findAll();

    UsuarioSistemaResponse findById(Long id);

    UsuarioSistemaResponse create(UsuarioSistemaRequest request);

    UsuarioSistemaResponse update(Long id, UsuarioSistemaRequest request);

    void delete(Long id);
}

