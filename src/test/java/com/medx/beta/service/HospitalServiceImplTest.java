package com.medx.beta.service;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.repository.HospitalRepository;
import com.medx.beta.service.impl.HospitalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospitalServiceImplTest {

    @Mock
    private HospitalRepository repository;

    @InjectMocks
    private HospitalServiceImpl service;

    private Hospital hospital;

    @BeforeEach
    void setUp() {
        hospital = new Hospital();
        hospital.setHospitalId(1);
        hospital.setNombre("General");
        hospital.setDescripcion("Desc");
    }

    @Test
    @DisplayName("getAll devuelve lista")
    void getAll_ok() {
        when(repository.findAll()).thenReturn(List.of(hospital));
        assertThat(service.getAll()).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("getById ok")
    void getById_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(hospital));
        Hospital found = service.getById(1);
        assertThat(found.getNombre()).isEqualTo("General");
    }

    @Test
    @DisplayName("getById NotFound")
    void getById_notFound() {
        when(repository.findById(9)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(9))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("create persiste hospital")
    void create_ok() {
        when(repository.save(hospital)).thenReturn(hospital);
        Hospital created = service.create(hospital);
        assertThat(created).isSameAs(hospital);
        verify(repository).save(hospital);
    }

    @Test
    @DisplayName("update modifica campos")
    void update_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(hospital));
        when(repository.save(any(Hospital.class))).thenAnswer(a -> a.getArgument(0));
        Hospital cambios = new Hospital();
        cambios.setNombre("Nuevo");
        cambios.setDescripcion("Nueva desc");
        Hospital updated = service.update(1, cambios);
        assertThat(updated.getNombre()).isEqualTo("Nuevo");
        assertThat(updated.getDescripcion()).contains("Nueva");
    }

    @Test
    @DisplayName("update NotFound")
    void update_notFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        Hospital cambios = new Hospital();
        cambios.setNombre("X");
        assertThatThrownBy(() -> service.update(2, cambios))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("deleteById ok")
    void delete_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(hospital));
        service.deleteHospital(1);
        verify(repository).delete(hospital);
    }

    @Test
    @DisplayName("deleteById NotFound")
    void delete_notFound() {
        when(repository.findById(7)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteHospital(7))
                .isInstanceOf(NotFoundException.class);
    }
}

