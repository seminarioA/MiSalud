package com.medx.beta.service.impl;

import com.medx.beta.dto.CitaMedicaRequest;
import com.medx.beta.dto.CitaMedicaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.CitaMedica;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.CitaMedicaRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceImplTest {

    @Mock private CitaMedicaRepository citaMedicaRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private PacienteRepository pacienteRepository;

    @InjectMocks private CitaMedicaServiceImpl service;

    @Test
    void getAll_mapsOk() {
        CitaMedica c = new CitaMedica();
        c.setCitaId(1);
        when(citaMedicaRepository.findAll()).thenReturn(List.of(c));

        List<CitaMedicaResponse> out = service.getAll();
        assertThat(out).hasSize(1);
        assertThat(out.get(0).getCitaId()).isEqualTo(1L);
    }

    @Test
    void getById_ok() {
        CitaMedica c = new CitaMedica();
        c.setCitaId(2);
        when(citaMedicaRepository.findById(2)).thenReturn(Optional.of(c));

        CitaMedicaResponse out = service.getById(2);
        assertThat(out.getCitaId()).isEqualTo(2L);
    }

    @Test
    void getById_notFound() {
        when(citaMedicaRepository.findById(9)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(9)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_ok() {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setFecha(LocalDateTime.now().plusDays(1));
        req.setCosto(new BigDecimal("10.00"));
        req.setDoctorId(1);
        req.setPacienteId(2);
        when(doctorRepository.findById(1)).thenReturn(Optional.of(new Doctor()));
        when(pacienteRepository.findById(2)).thenReturn(Optional.of(new Paciente()));
        CitaMedica saved = new CitaMedica();
        saved.setCitaId(10);
        when(citaMedicaRepository.save(any(CitaMedica.class))).thenReturn(saved);

        CitaMedicaResponse out = service.create(req);
        assertThat(out.getCitaId()).isEqualTo(10L);
        verify(citaMedicaRepository).save(any(CitaMedica.class));
    }

    @Test
    void create_missingDoctor_throws() {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setDoctorId(1);
        req.setPacienteId(2);
        when(doctorRepository.findById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.create(req)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_ok() {
        CitaMedica existente = new CitaMedica();
        existente.setCitaId(5);
        when(citaMedicaRepository.findById(5)).thenReturn(Optional.of(existente));
        when(doctorRepository.findById(1)).thenReturn(Optional.of(new Doctor()));
        when(pacienteRepository.findById(2)).thenReturn(Optional.of(new Paciente()));
        when(citaMedicaRepository.save(any(CitaMedica.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setFecha(LocalDateTime.now());
        req.setCosto(new BigDecimal("15.00"));
        req.setDoctorId(1);
        req.setPacienteId(2);

        CitaMedicaResponse out = service.update(5, req);
        assertThat(out.getCitaId()).isEqualTo(5L);
    }

    @Test
    void update_notFound() {
        when(citaMedicaRepository.findById(55)).thenReturn(Optional.empty());
        CitaMedicaRequest req = new CitaMedicaRequest();
        assertThatThrownBy(() -> service.update(55, req)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_ok() {
        CitaMedica existente = new CitaMedica();
        existente.setCitaId(3);
        when(citaMedicaRepository.findById(3)).thenReturn(Optional.of(existente));

        service.deleteById(3);
        verify(citaMedicaRepository).delete(any(CitaMedica.class));
    }

    @Test
    void delete_notFound() {
        when(citaMedicaRepository.findById(33)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteById(33)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void finders_mapOk() {
        CitaMedica c = new CitaMedica();
        c.setCitaId(7);
        when(citaMedicaRepository.findByDoctor_DoctorId(1)).thenReturn(List.of(c));
        when(citaMedicaRepository.findByPaciente_PacienteId(2)).thenReturn(List.of(c));
        when(citaMedicaRepository.findByFechaBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(c));

        assertThat(service.getByDoctor(1)).hasSize(1);
        assertThat(service.getByPaciente(2)).hasSize(1);
        assertThat(service.getByFechaBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now())).hasSize(1);
    }
}

