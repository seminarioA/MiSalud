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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceImplTest {

    @Mock
    private CitaMedicaRepository citaMedicaRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private CitaMedicaServiceImpl service;

    @Test
    void getAll_mapsOk() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        Paciente paciente = new Paciente();
        paciente.setPacienteId(2);
        LocalDateTime fecha = LocalDateTime.now();

        CitaMedica cita = new CitaMedica();
        cita.setCitaId(1);
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setFecha(fecha);
        cita.setTipoCita(CitaMedica.TipoCita.PRESENCIAL);
        cita.setEstado(CitaMedica.EstadoCita.Confirmada);
        cita.setCosto(new BigDecimal("20.00"));
        cita.setFechaCreacion(fecha.minusDays(1));
        cita.setFechaActualizacion(fecha);
        when(citaMedicaRepository.findAll()).thenReturn(List.of(cita));

        List<CitaMedicaResponse> out = service.getAll();

        assertThat(out).hasSize(1);
        CitaMedicaResponse dto = out.get(0);
        assertThat(dto.getCitaId()).isEqualTo(1);
        assertThat(dto.getDoctorId()).isEqualTo(1);
        assertThat(dto.getPacienteId()).isEqualTo(2);
        assertThat(dto.getTipoCita()).isEqualTo(CitaMedica.TipoCita.PRESENCIAL);
        assertThat(dto.getEstado()).isEqualTo(CitaMedica.EstadoCita.Confirmada);
        assertThat(dto.getCosto()).isEqualByComparingTo("20.00");
        assertThat(dto.getFechaCreacion()).isEqualTo(fecha.minusDays(1));
        assertThat(dto.getFechaActualizacion()).isEqualTo(fecha);
    }

    @Test
    void getById_ok() {
        CitaMedica cita = new CitaMedica();
        cita.setCitaId(2);
        when(citaMedicaRepository.findById(2)).thenReturn(Optional.of(cita));

        CitaMedicaResponse out = service.getById(2);

        assertThat(out.getCitaId()).isEqualTo(2);
    }

    @Test
    void getById_notFound() {
        when(citaMedicaRepository.findById(9)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("9");
    }

    @Test
    void create_ok() {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setFecha(LocalDateTime.now().plusDays(1));
        req.setCosto(new BigDecimal("10.00"));
        req.setDoctorId(1);
        req.setPacienteId(2);
        req.setTipoCita(CitaMedica.TipoCita.TELEMEDICINA);
        req.setEstado(CitaMedica.EstadoCita.Reservada);
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        Paciente paciente = new Paciente();
        paciente.setPacienteId(2);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(1)).thenReturn(Optional.of(doctor));
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(2)).thenReturn(Optional.of(paciente));
        CitaMedica saved = new CitaMedica();
        saved.setCitaId(10);
        saved.setDoctor(doctor);
        saved.setPaciente(paciente);
        when(citaMedicaRepository.save(any(CitaMedica.class))).thenReturn(saved);

        CitaMedicaResponse out = service.create(req);

        assertThat(out.getCitaId()).isEqualTo(10);
        assertThat(out.getDoctorId()).isEqualTo(1);
        assertThat(out.getPacienteId()).isEqualTo(2);
        verify(doctorRepository).findByDoctorIdAndEstaActivoTrue(1);
        verify(pacienteRepository).findByPacienteIdAndEstaActivoTrue(2);
        verify(citaMedicaRepository).save(any(CitaMedica.class));
    }

    @Test
    void create_missingDoctor_throws() {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setDoctorId(1);
        req.setPacienteId(2);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Doctor");
        verify(pacienteRepository, never()).findByPacienteIdAndEstaActivoTrue(any());
    }

    @Test
    void create_missingPaciente_throws() {
        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setDoctorId(1);
        req.setPacienteId(2);
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(1)).thenReturn(Optional.of(doctor));
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(2)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Paciente");
        verify(citaMedicaRepository, never()).save(any());
    }

    @Test
    void update_ok() {
        CitaMedica existente = new CitaMedica();
        existente.setCitaId(5);
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        Paciente paciente = new Paciente();
        paciente.setPacienteId(2);
        when(citaMedicaRepository.findById(5)).thenReturn(Optional.of(existente));
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(1)).thenReturn(Optional.of(doctor));
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(2)).thenReturn(Optional.of(paciente));
        when(citaMedicaRepository.save(any(CitaMedica.class))).thenAnswer(inv -> inv.getArgument(0));

        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setFecha(LocalDateTime.now());
        req.setCosto(new BigDecimal("15.00"));
        req.setDoctorId(1);
        req.setPacienteId(2);
        req.setTipoCita(CitaMedica.TipoCita.PRESENCIAL);
        req.setEstado(CitaMedica.EstadoCita.Confirmada);

        CitaMedicaResponse out = service.update(5, req);

        assertThat(out.getCitaId()).isEqualTo(5);
        assertThat(out.getDoctorId()).isEqualTo(1);
        assertThat(out.getPacienteId()).isEqualTo(2);
        verify(citaMedicaRepository).save(existente);
    }

    @Test
    void update_notFound() {
        when(citaMedicaRepository.findById(55)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(55, new CitaMedicaRequest()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_missingDoctor() {
        CitaMedica existente = new CitaMedica();
        existente.setCitaId(6);
        when(citaMedicaRepository.findById(6)).thenReturn(Optional.of(existente));
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(1)).thenReturn(Optional.empty());

        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setDoctorId(1);
        req.setPacienteId(2);

        assertThatThrownBy(() -> service.update(6, req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Doctor");
        verify(pacienteRepository, never()).findByPacienteIdAndEstaActivoTrue(any());
    }

    @Test
    void update_missingPaciente() {
        CitaMedica existente = new CitaMedica();
        existente.setCitaId(6);
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        when(citaMedicaRepository.findById(6)).thenReturn(Optional.of(existente));
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(1)).thenReturn(Optional.of(doctor));
        when(pacienteRepository.findByPacienteIdAndEstaActivoTrue(2)).thenReturn(Optional.empty());

        CitaMedicaRequest req = new CitaMedicaRequest();
        req.setDoctorId(1);
        req.setPacienteId(2);

        assertThatThrownBy(() -> service.update(6, req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Paciente");
        verify(citaMedicaRepository, never()).save(any());
    }

    @Test
    void delete_ok() {
        CitaMedica existente = new CitaMedica();
        existente.setCitaId(3);
        when(citaMedicaRepository.findById(3)).thenReturn(Optional.of(existente));

        service.deleteById(3);

        verify(citaMedicaRepository).delete(existente);
    }

    @Test
    void delete_notFound() {
        when(citaMedicaRepository.findById(33)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(33)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void finders_mapOk() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        Paciente paciente = new Paciente();
        paciente.setPacienteId(2);
        CitaMedica cita = new CitaMedica();
        cita.setCitaId(7);
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        when(citaMedicaRepository.findByDoctor_DoctorId(1)).thenReturn(List.of(cita));
        when(citaMedicaRepository.findByPaciente_PacienteId(2)).thenReturn(List.of(cita));
        when(citaMedicaRepository.findByFechaBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(cita));

        assertThat(service.getByDoctor(1)).hasSize(1);
        assertThat(service.getByPaciente(2)).hasSize(1);
        assertThat(service.getByFechaBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now())).hasSize(1);
    }
}
