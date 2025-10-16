package com.medx.beta.service.impl;

import com.medx.beta.model.Usuario;
import com.medx.beta.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl service;

    @Test
    void loadUserByUsername_devuelveUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario");
        usuario.setPassword("pwd");
        when(usuarioRepository.findByUsername("usuario")).thenReturn(Optional.of(usuario));

        UserDetails details = service.loadUserByUsername("usuario");

        assertThat(details.getUsername()).isEqualTo("usuario");
        assertThat(details.getPassword()).isEqualTo("pwd");
    }

    @Test
    void loadUserByUsername_lanzaSiNoExiste() {
        when(usuarioRepository.findByUsername("usuario")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("usuario"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuario no encontrado: usuario");
    }
}

