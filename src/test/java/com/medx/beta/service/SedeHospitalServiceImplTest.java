package com.medx.beta.service;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.SedeHospitalRepository;
import com.medx.beta.service.impl.SedeHospitalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SedeHospitalServiceImplTest {

    @Mock
    private SedeHospitalRepository repository;

    @InjectMocks
    private SedeHospitalServiceImpl service;

    private SedeHospital sede;
    private Hospital hospital;

    @BeforeEach
    void setUp() {
        hospital = new Hospital();
        hospital.setHospitalId(3);
        hospital.setNombre("Central");

        sede = new SedeHospital();
        sede.setSedeId(1);
        sede.setSede("Sede Norte");
        sede.setUbicacion("Av. Principal 100");
        sede.setHospital(hospital);
    }

    @Test
    @DisplayName("getAll retorna lista")
    void getAll_ok() {
        when(repository.findAll()).thenReturn(List.of(sede));
        assertThat(service.getAll()).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("getById existente")
    void getById_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(sede));
        SedeHospital found = service.getById(1);
        assertThat(found.getSede()).isEqualTo("Sede Norte");
    }

    @Test
    @DisplayName("getById NotFound")
    void getById_notFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("create fuerza id null y persiste")
    void create_ok() {
        SedeHospital nueva = new SedeHospital();
        nueva.setSedeId(88);
        nueva.setSede("Sede Sur");
        nueva.setUbicacion("Calle 2");
        nueva.setHospital(hospital);
        when(repository.save(any(SedeHospital.class))).thenAnswer(a -> {
            SedeHospital in = a.getArgument(0);
            SedeHospital persisted = new SedeHospital();
            persisted.setSedeId(5);
            persisted.setSede(in.getSede());
            persisted.setUbicacion(in.getUbicacion());
            persisted.setHospital(in.getHospital());
            return persisted;
        });
        SedeHospital creada = service.create(nueva);
        assertThat(creada.getSedeId()).isEqualTo(5);
        ArgumentCaptor<SedeHospital> captor = ArgumentCaptor.forClass(SedeHospital.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getSedeId()).isNull();
    }

    @Test
    @DisplayName("update modifica campos")
    void update_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(sede));
        when(repository.save(any(SedeHospital.class))).thenAnswer(a -> a.getArgument(0));
        SedeHospital cambios = new SedeHospital();
        cambios.setSede("Sede Norte Mod");
        cambios.setUbicacion("Nueva ubicacion");
        cambios.setHospital(hospital);
        SedeHospital updated = service.update(1, cambios);
        assertThat(updated.getSede()).contains("Mod");
        assertThat(updated.getUbicacion()).contains("Nueva");
    }

    @Test
    @DisplayName("update NotFound")
    void update_notFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(2, sede))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("deleteById ok")
    void delete_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(sede));
        service.deleteById(1);
        verify(repository).delete(sede);
    }

    @Test
    @DisplayName("deleteById NotFound")
    void delete_notFound() {
        when(repository.findById(7)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteById(7))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("getByHospital delega en repository")
    void getByHospital_ok() {
        when(repository.findByHospital_HospitalId(3)).thenReturn(List.of(sede));
        assertThat(service.getByHospital(3)).hasSize(1);
        verify(repository).findByHospital_HospitalId(3);
    }
}
