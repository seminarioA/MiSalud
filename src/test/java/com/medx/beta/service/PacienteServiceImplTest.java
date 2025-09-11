package com.medx.beta.service;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PacienteServiceImpl Tests")
class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteServiceImpl service;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setPacienteId(1);
        paciente.setPrimerNombre("Ana");
        paciente.setSegundoNombre("Maria");
        paciente.setPrimerApellido("Lopez");
        paciente.setSegundoApellido("Diaz");
        paciente.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        paciente.setDomicilio("Calle Salud 100");
        paciente.setEstaActivo(true);
    }

    @AfterEach
    void verifyNoExtra() {
        verifyNoMoreInteractions(pacienteRepository);
    }

    @Test
    @DisplayName("getAll retorna lista de pacientes")
    void getAll_ok() {
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));
        List<Paciente> lista = service.getAll();
        assertThat(lista).hasSize(1).first().extracting(Paciente::getPacienteId).isEqualTo(1);
        verify(pacienteRepository).findAll();
    }

    @Test
    @DisplayName("getById retorna paciente existente")
    void getById_ok() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));
        Paciente found = service.getById(1);
        assertThat(found.getPrimerNombre()).isEqualTo("Ana");
        verify(pacienteRepository).findById(1);
    }

    @Test
    @DisplayName("getById lanza NotFoundException si no existe")
    void getById_notFound() {
        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
        verify(pacienteRepository).findById(99);
    }

    @Test
    @DisplayName("create fuerza id null y persiste")
    void create_ok() {
        Paciente nuevo = new Paciente();
        nuevo.setPacienteId(500); // debe ignorarse
        nuevo.setPrimerNombre("Luis");
        nuevo.setPrimerApellido("Rojas");
        nuevo.setSegundoApellido("Mena");
        nuevo.setEstaActivo(true);

        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> {
            Paciente p = inv.getArgument(0);
            p.setPacienteId(10);
            return p;
        });

        Paciente creado = service.create(nuevo);
        assertThat(creado.getPacienteId()).isEqualTo(10);

        ArgumentCaptor<Paciente> captor = ArgumentCaptor.forClass(Paciente.class);
        verify(pacienteRepository).save(captor.capture());
        assertThat(captor.getValue().getPacienteId()).as("ID debe forzarse a null en create").isNull();
    }

    @Test
    @DisplayName("update modifica campos y persiste")
    void update_ok() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        Paciente cambios = new Paciente();
        cambios.setPrimerNombre("AnaMaria");
        cambios.setSegundoNombre("M.");
        cambios.setPrimerApellido("Lopez");
        cambios.setSegundoApellido("Diaz");
        cambios.setFechaNacimiento(LocalDate.of(1995, 5, 20));
        cambios.setDomicilio("Nueva direccion 200");
        cambios.setEstaActivo(false);

        Paciente actualizado = service.update(1, cambios);
        assertThat(actualizado.getPrimerNombre()).isEqualTo("AnaMaria");
        assertThat(actualizado.getEstaActivo()).isFalse();
        assertThat(actualizado.getDomicilio()).contains("Nueva direccion");

        verify(pacienteRepository).findById(1);
        verify(pacienteRepository).save(paciente);
    }

    @Test
    @DisplayName("update lanza NotFound si paciente no existe")
    void update_notFound() {
        when(pacienteRepository.findById(2)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(2, paciente))
                .isInstanceOf(NotFoundException.class);
        verify(pacienteRepository).findById(2);
    }

    @Test
    @DisplayName("deleteById elimina cuando existe")
    void delete_ok() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));
        service.deleteById(1);
        verify(pacienteRepository).findById(1);
        verify(pacienteRepository).delete(paciente);
    }

    @Test
    @DisplayName("deleteById lanza NotFound cuando no existe")
    void delete_notFound() {
        when(pacienteRepository.findById(50)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteById(50))
                .isInstanceOf(NotFoundException.class);
        verify(pacienteRepository).findById(50);
    }
}

