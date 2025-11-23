package com.medx.beta.service.impl;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioSistemaRepository usuarioSistemaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioSistemaRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
}

