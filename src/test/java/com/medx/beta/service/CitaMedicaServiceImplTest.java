package com.medx.beta.service;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.CitaMedica;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.CitaMedicaRepository;
import com.medx.beta.service.impl.CitaMedicaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceImplTest {

    @Mock
    private CitaMedicaRepository repository;

    @InjectMocks
    private CitaMedicaServiceImpl service;

    private CitaMedica cita;
    private Doctor doctor;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setDoctorId(1);
        paciente = new Paciente();
        paciente.setPacienteId(2);
        cita = new CitaMedica();
        cita.setCitaId(10);
        cita.setFecha(LocalDateTime.of(2025,1,1,10,0));
        cita.setCosto(new BigDecimal("100.00"));
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
    }

    @Test
    @DisplayName("getAll retorna lista")
    void getAll_ok() {
        when(repository.findAll()).thenReturn(List.of(cita));
        assertThat(service.getAll()).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("getById existente")
    void getById_ok() {
        when(repository.findById(10)).thenReturn(Optional.of(cita));
        CitaMedica found = service.getById(10);
        assertThat(found.getCosto()).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("getById NotFound")
    void getById_notFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("create fuerza id null y guarda")
    void create_ok() {
        CitaMedica nueva = new CitaMedica();
        nueva.setCitaId(500);
        nueva.setFecha(LocalDateTime.now());
        nueva.setCosto(new BigDecimal("50.00"));
        nueva.setDoctor(doctor);
        nueva.setPaciente(paciente);
        when(repository.save(any(CitaMedica.class))).thenAnswer(a -> { CitaMedica c = a.getArgument(0); c.setCitaId(123); return c;});
        CitaMedica creada = service.create(nueva);
        assertThat(creada.getCitaId()).isEqualTo(123);
        ArgumentCaptor<CitaMedica> captor = ArgumentCaptor.forClass(CitaMedica.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getCitaId()).isNull();
    }

    @Test
    @DisplayName("update modifica campos basicos")
    void update_ok() {
        when(repository.findById(10)).thenReturn(Optional.of(cita));
        when(repository.save(any(CitaMedica.class))).thenAnswer(a -> a.getArgument(0));
        CitaMedica cambios = new CitaMedica();
        cambios.setFecha(LocalDateTime.of(2025,2,2,14,30));
        cambios.setCosto(new BigDecimal("200.00"));
        cambios.setDoctor(doctor);
        cambios.setPaciente(paciente);
        CitaMedica updated = service.update(10, cambios);
        assertThat(updated.getFecha().getMonthValue()).isEqualTo(2);
        assertThat(updated.getCosto()).isEqualByComparingTo("200.00");
    }

    @Test
    @DisplayName("update NotFound")
    void update_notFound() {
        when(repository.findById(44)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(44, cita))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("deleteById ok")
    void delete_ok() {
        when(repository.findById(10)).thenReturn(Optional.of(cita));
        service.deleteById(10);
        verify(repository).delete(cita);
    }

    @Test
    @DisplayName("deleteById NotFound")
    void delete_notFound() {
        when(repository.findById(11)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteById(11))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("getByDoctor delega al repository")
    void getByDoctor_ok() {
        when(repository.findByDoctor_DoctorId(1)).thenReturn(List.of(cita));
        assertThat(service.getByDoctor(1)).hasSize(1);
        verify(repository).findByDoctor_DoctorId(1);
    }

    @Test
    @DisplayName("getByPaciente delega al repository")
    void getByPaciente_ok() {
        when(repository.findByPaciente_PacienteId(2)).thenReturn(List.of(cita));
        assertThat(service.getByPaciente(2)).hasSize(1);
        verify(repository).findByPaciente_PacienteId(2);
    }

    @Test
    @DisplayName("getByFechaBetween filtra por rango")
    void getByFechaBetween_ok() {
        LocalDateTime inicio = LocalDateTime.of(2024,12,31,0,0);
        LocalDateTime fin = LocalDateTime.of(2025,12,31,23,59);
        when(repository.findByFechaBetween(inicio, fin)).thenReturn(List.of(cita));
        assertThat(service.getByFechaBetween(inicio, fin)).hasSize(1);
        verify(repository).findByFechaBetween(inicio, fin);
    }
}

