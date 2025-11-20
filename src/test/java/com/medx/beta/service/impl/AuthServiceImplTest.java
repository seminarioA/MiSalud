package com.medx.beta.service.impl;

import com.medx.beta.dto.AuthResponse;
import com.medx.beta.dto.LoginRequest;
import com.medx.beta.dto.RegistroRequest;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.repository.UsuarioRepository;
import com.medx.beta.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @AfterEach
    void cleanupContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registrarUsuario_creaPacienteConPasswordEncriptada() {
        RegistroRequest request = buildRegistroRequest();
        when(usuarioRepository.existsByUsername("usuario01")).thenReturn(false);
        when(usuarioRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Abcd1234!"))
                .thenReturn("encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setUsuarioId(15L);
            return usuario;
        });

        Usuario creado = authService.registrarUsuario(request);

        assertThat(creado.getUsuarioId()).isEqualTo(15L);
        assertThat(creado.getPassword()).isEqualTo("encoded");
        assertThat(creado.getRol()).isEqualTo(Usuario.Role.PACIENTE);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_fallaSiPasswordsNoCoinciden() {
        RegistroRequest request = buildRegistroRequest();
        request.setConfirmarPassword("otro");

        assertThatThrownBy(() -> authService.registrarUsuario(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Las contraseñas no coinciden");
    }

    @Test
    void registrarUsuario_fallaSiUsernameExiste() {
        RegistroRequest request = buildRegistroRequest();
        when(usuarioRepository.existsByUsername("usuario01")).thenReturn(true);

        assertThatThrownBy(() -> authService.registrarUsuario(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre de usuario ya está en uso");
    }

    @Test
    void registrarUsuario_fallaSiEmailExiste() {
        RegistroRequest request = buildRegistroRequest();
        when(usuarioRepository.existsByUsername("usuario01")).thenReturn(false);
        when(usuarioRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registrarUsuario(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El email ya está registrado");
    }

    @Test
    void autenticar_devuelveTokenYDatosUsuario() {
        Usuario usuario = buildUsuario();
        when(usuarioRepository.findByUsernameOrEmail("usuario01", "usuario01"))
                .thenReturn(Optional.of(usuario));
        Authentication auth = new UsernamePasswordAuthenticationToken("usuario01", "Abcd1234!");
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(jwtService.generateToken(usuario)).thenReturn("token-123");
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("usuario01");
        request.setPassword("Abcd1234!");

        AuthResponse response = authService.autenticar(request);

        assertThat(response.token()).isEqualTo("token-123");
        assertThat(response.username()).isEqualTo("usuario01");
        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtService).generateToken(usuario);
    }

    @Test
    void autenticar_lanzaCuandoUsuarioNoExiste() {
        when(usuarioRepository.findByUsernameOrEmail(any(), any()))
                .thenReturn(Optional.empty());
        LoginRequest request = new LoginRequest();
        request.setUsernameOrEmail("inexistente");
        request.setPassword("pwd");

        assertThatThrownBy(() -> authService.autenticar(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Usuario no encontrado");
        verify(authenticationManager, never()).authenticate(any(Authentication.class));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void obtenerUsuarioActual_devuelveUsuarioDelContexto() {
        Usuario usuario = buildUsuario();
        when(usuarioRepository.findByUsername("usuario01")).thenReturn(Optional.of(usuario));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("usuario01", "pwd"));
        SecurityContextHolder.setContext(context);

        Usuario actual = authService.obtenerUsuarioActual();

        assertThat(actual).isSameAs(usuario);
        verify(usuarioRepository).findByUsername("usuario01");
    }

    @Test
    void obtenerUsuarioActual_lanzaSiNoExiste() {
        when(usuarioRepository.findByUsername("usuario01")).thenReturn(Optional.empty());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("usuario01", "pwd"));
        SecurityContextHolder.setContext(context);

        assertThatThrownBy(() -> authService.obtenerUsuarioActual())
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Usuario no encontrado");
    }

    private RegistroRequest buildRegistroRequest() {
        RegistroRequest request = new RegistroRequest();
        request.setUsername("usuario01");
        request.setEmail("user@example.com");
        request.setPassword("Abcd1234!");
        request.setConfirmarPassword("Abcd1234!");
        request.setNombre("Juan");
        request.setApellido("Pérez");
        return request;
    }

    private Usuario buildUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(3L);
        usuario.setUsername("usuario01");
        usuario.setEmail("user@example.com");
        usuario.setPassword("encoded");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setRol(Usuario.Role.PACIENTE);
        return usuario;
    }
}
