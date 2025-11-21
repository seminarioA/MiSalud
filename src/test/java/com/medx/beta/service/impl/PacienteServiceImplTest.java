package com.medx.beta.service.impl;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteServiceImpl service;

    @Test
    void getAll_mapsEntities() {
        Paciente paciente = new Paciente();
        paciente.setPacienteId(1);
        paciente.setPrimerNombre("Ana");
        paciente.setPrimerApellido("Ruiz");
        paciente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        paciente.setEstaActivo(true);
        paciente.setFechaCreacion(LocalDateTime.now().minusDays(2));
        paciente.setFechaActualizacion(LocalDateTime.now().minusDays(1));
        when(pacienteRepository.findAllActivos()).thenReturn(List.of(paciente));

        List<PacienteResponse> out = service.getAll();

        assertThat(out).hasSize(1);
        PacienteResponse dto = out.get(0);
        assertThat(dto.getPacienteId()).isEqualTo(1);
        assertThat(dto.getPrimerNombre()).isEqualTo("Ana");
        assertThat(dto.getPrimerApellido()).isEqualTo("Ruiz");
        assertThat(dto.getEstaActivo()).isTrue();
    }

    @Test
    void getById_ok() {
        Paciente paciente = new Paciente();
        paciente.setPacienteId(5);
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(5)).thenReturn(Optional.of(paciente));

        PacienteResponse out = service.getById(5);

        assertThat(out.getPacienteId()).isEqualTo(5);
    }

    @Test
    void getById_notFound() {
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(9)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("9");
    }

    @Test
    void create_ok() {
        PacienteRequest req = new PacienteRequest();
        req.setPrimerNombre("Ana");
        req.setSegundoNombre("Maria");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        req.setDomicilio("Calle 1");
        Paciente saved = new Paciente();
        saved.setPacienteId(10);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(saved);

        PacienteResponse out = service.create(req);

        assertThat(out.getPacienteId()).isEqualTo(10);
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void update_ok() {
        Paciente existente = new Paciente();
        existente.setPacienteId(7);
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(7)).thenReturn(Optional.of(existente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        PacienteRequest req = new PacienteRequest();
        req.setPrimerNombre("Ana");
        req.setSegundoNombre("Maria");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        req.setDomicilio("Calle 2");

        PacienteResponse out = service.update(7, req);

        assertThat(out.getPacienteId()).isEqualTo(7);
        assertThat(out.getDomicilio()).isEqualTo("Calle 2");
        verify(pacienteRepository).save(existente);
    }

    @Test
    void update_notFound() {
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(11)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(11, new PacienteRequest()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_ok() {
        Paciente existente = new Paciente();
        existente.setPacienteId(4);
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(4)).thenReturn(Optional.of(existente));

        service.deleteById(4);

        verify(pacienteRepository).desactivarPorId(4);
    }

    @Test
    void delete_notFound() {
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(44)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(44)).isInstanceOf(NotFoundException.class);
        verify(pacienteRepository, never()).desactivarPorId(any());
    }
}
