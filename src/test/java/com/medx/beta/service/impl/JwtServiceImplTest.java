package com.medx.beta.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
    }

    @Test
    void generateToken_yExtraeUsername() {
        UserDetails userDetails = User.withUsername("usuario")
                .password("pwd")
                .roles("PACIENTE")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.extractUsername(token)).isEqualTo("usuario");
        assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
    }

    @Test
    void isTokenExpired_devuelveTrueParaTokenAntiguo() {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        UserDetails userDetails = User.withUsername("usuario")
                .password("pwd")
                .roles("PACIENTE")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.isTokenExpired(token)).isTrue();
        assertThat(jwtService.isTokenValid(token, userDetails)).isFalse();
    }
}

