package com.medx.beta.service;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl service;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setDoctorId(1);
        doctor.setPrimerNombre("Juan");
        doctor.setPrimerApellido("Perez");
        doctor.setSegundoApellido("Gomez");
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(5);
        doctor.setSedeHospital(sede);
    }

    @Test
    @DisplayName("getAll debe delegar en repository.findAll")
    void getAll_ok() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));
        List<Doctor> result = service.getAll();
        assertThat(result).hasSize(1).first().extracting(Doctor::getDoctorId).isEqualTo(1);
        verify(doctorRepository).findAll();
    }

    @Test
    @DisplayName("getById retorna el doctor cuando existe")
    void getById_ok() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        Doctor found = service.getById(1);
        assertThat(found.getPrimerNombre()).isEqualTo("Juan");
        verify(doctorRepository).findById(1);
    }

    @Test
    @DisplayName("getById lanza NotFoundException cuando no existe")
    void getById_notFound() {
        when(doctorRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
        verify(doctorRepository).findById(99);
    }

    @Test
    @DisplayName("create fuerza id null y guarda")
    void create_ok() {
        Doctor toCreate = new Doctor();
        toCreate.setDoctorId(999); // debe ser sobreescrito a null
        toCreate.setPrimerNombre("Luis");
        toCreate.setPrimerApellido("Ramos");
        toCreate.setSegundoApellido("Diaz");
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(2);
        toCreate.setSedeHospital(sede);

        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> {
            Doctor d = inv.getArgument(0);
            d.setDoctorId(10);
            return d;
        });

        Doctor created = service.create(toCreate);
        assertThat(created.getDoctorId()).isEqualTo(10);
        // capturar para validar que se envió con id null
        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
        verify(doctorRepository).save(captor.capture());
        assertThat(captor.getValue().getDoctorId()).isNull();
    }

    @Test
    @DisplayName("update modifica campos y persiste")
    void update_ok() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        Doctor cambios = new Doctor();
        cambios.setPrimerNombre("Carlos");
        cambios.setPrimerApellido("Perez");
        cambios.setSegundoApellido("Gomez");
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(7);
        cambios.setSedeHospital(sede);

        Doctor actualizado = service.update(1, cambios);
        assertThat(actualizado.getPrimerNombre()).isEqualTo("Carlos");
        assertThat(actualizado.getSedeHospital().getSedeId()).isEqualTo(7);
        verify(doctorRepository).save(doctor);
    }

    @Test
    @DisplayName("update lanza NotFound cuando no existe")
    void update_notFound() {
        when(doctorRepository.findById(55)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(55, doctor))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("deleteById elimina cuando existe")
    void delete_ok() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        service.deleteById(1);
        verify(doctorRepository).delete(doctor);
    }

    @Test
    @DisplayName("deleteById lanza NotFound cuando no existe")
    void delete_notFound() {
        when(doctorRepository.findById(77)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteById(77))
                .isInstanceOf(NotFoundException.class);
    }
}

