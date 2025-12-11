package com.medx.beta.config;

import com.medx.beta.model.Seguro;
import com.medx.beta.repository.SeguroRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import com.medx.beta.model.Persona;
import com.medx.beta.model.UsuarioSistema;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.repository.UsuarioSistemaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ReferenceDataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataLoader.class);

    private final SeguroRepository seguroRepository;
    private final PersonaRepository personaRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedSeguros();
        seedUsuarioBryan();
    }

    private void seedSeguros() {
        List<SeguroSeed> seeds = List.of(
                new SeguroSeed("ESSALUD", "PUBLICO", new BigDecimal("0.90"), new BigDecimal("10.00")),
                new SeguroSeed("SIS", "PUBLICO", new BigDecimal("1.00"), BigDecimal.ZERO),
                new SeguroSeed("EPS Rimac", "EPS", new BigDecimal("0.80"), new BigDecimal("25.00")),
                new SeguroSeed("EPS Pacifico", "EPS", new BigDecimal("0.85"), new BigDecimal("20.00")),
                new SeguroSeed("MAPFRE Salud", "PRIVADO", new BigDecimal("0.75"), new BigDecimal("30.00")),
                new SeguroSeed("ATENCION PARTICULAR", "PARTICULAR", BigDecimal.ZERO, BigDecimal.ZERO)
        );

        seeds.forEach(seed -> seguroRepository.findByNombreAseguradora(seed.nombreAseguradora)
                .orElseGet(() -> {
                    Seguro saved = seguroRepository.save(Seguro.builder()
                            .nombreAseguradora(seed.nombreAseguradora)
                            .tipoSeguro(seed.tipoSeguro)
                            .coberturaPorcentaje(seed.coberturaPorcentaje)
                            .copagoFijo(seed.copagoFijo)
                            .build());
                    LOGGER.info("Seguro de referencia '{}' registrado", saved.getNombreAseguradora());
                    return saved;
                }));
    }

    // Seeder para el usuario Bryan Joel Yovera Vilchez (21 años, masculino)
    private void seedUsuarioBryan() {
        final String email = "yoverabryan@gmail.com";
        final String rawPassword = "finalAngular?2025";
        // Si ya existe un usuario con ese email, no sembrar de nuevo
        if (usuarioSistemaRepository.findByEmail(email).isPresent()) {
            LOGGER.info("Usuario de referencia '{}' ya existe, omitiendo seeding", email);
            return;
        }

        // Construir Persona requerida (documento y campos obligatorios)
        Persona persona = Persona.builder()
                .primerNombre("Bryan")
                .segundoNombre("Joel")
                .primerApellido("Yovera")
                .segundoApellido("Vilchez")
                .tipoDocumento(Persona.TipoDocumento.DNI)
                .numeroDocumento("12345678")
                // 21 años al 2025-12-10
                .fechaNacimiento(LocalDate.of(2004, 12, 10))
                .genero(Persona.Genero.MASCULINO)
                .numeroTelefono(null)
                .urlFotoPerfil(null)
                .build();
        persona = personaRepository.save(persona);

        UsuarioSistema usuario = UsuarioSistema.builder()
                .persona(persona)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .rol(UsuarioSistema.Rol.OPERACIONES)
                .build();
        usuarioSistemaRepository.save(usuario);
        LOGGER.info("Usuario de referencia '{}' registrado con rol {}", email, usuario.getRol());
    }

    private record SeguroSeed(String nombreAseguradora, String tipoSeguro, BigDecimal coberturaPorcentaje,
                              BigDecimal copagoFijo) {
    }
}
