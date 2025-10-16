package com.medx.beta.service.impl;

import com.medx.beta.dto.HospitalRequest;
import com.medx.beta.dto.HospitalResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.repository.HospitalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HospitalServiceImplTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalServiceImpl service;

    @Test
    void getAll_mapsEntitiesToResponses() {
        Hospital h = new Hospital();
        h.setHospitalId(1);
        h.setNombre("General");
        h.setDescripcion("Desc");
        h.setFechaCreacion(LocalDateTime.now());
        when(hospitalRepository.findAll()).thenReturn(List.of(h));

        List<HospitalResponse> out = service.getAll();

        assertThat(out).hasSize(1);
        assertThat(out.get(0).getHospitalId()).isEqualTo(1);
        assertThat(out.get(0).getNombre()).isEqualTo("General");
    }

    @Test
    void getById_ok() {
        Hospital h = new Hospital();
        h.setHospitalId(2);
        h.setNombre("A");
        when(hospitalRepository.findById(2)).thenReturn(Optional.of(h));

        HospitalResponse out = service.getById(2);

        assertThat(out.getHospitalId()).isEqualTo(2);
        assertThat(out.getNombre()).isEqualTo("A");
    }

    @Test
    void getById_notFound_throws() {
        when(hospitalRepository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_persistsAndMaps() {
        HospitalRequest req = new HospitalRequest();
        req.setNombre("Nuevo");
        req.setDescripcion("D");
        Hospital saved = new Hospital();
        saved.setHospitalId(10);
        saved.setNombre("Nuevo");
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(saved);

        HospitalResponse out = service.create(req);

        assertThat(out.getHospitalId()).isEqualTo(10);
        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    void update_ok() {
        Hospital existing = new Hospital();
        existing.setHospitalId(5);
        when(hospitalRepository.findById(5)).thenReturn(Optional.of(existing));
        when(hospitalRepository.save(any(Hospital.class))).thenAnswer(inv -> inv.getArgument(0));
        HospitalRequest req = new HospitalRequest();
        req.setNombre("Up");
        req.setDescripcion("D");

        HospitalResponse out = service.update(5, req);

        assertThat(out.getNombre()).isEqualTo("Up");
        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    void deleteById_callsRepository() {
        service.deleteById(3);
        verify(hospitalRepository).deleteById(eq(3));
    }
}

