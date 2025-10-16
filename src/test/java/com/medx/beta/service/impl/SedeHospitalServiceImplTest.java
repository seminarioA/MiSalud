package com.medx.beta.service.impl;

import com.medx.beta.dto.SedeHospitalRequest;
import com.medx.beta.dto.SedeHospitalResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.HospitalRepository;
import com.medx.beta.repository.SedeHospitalRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SedeHospitalServiceImplTest {

    @Mock
    private SedeHospitalRepository sedeHospitalRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private SedeHospitalServiceImpl service;

    @Test
    void getAll_mapeaTodosLosCampos() {
        SedeHospital sede = buildSedeHospital();
        when(sedeHospitalRepository.findAll()).thenReturn(List.of(sede));

        List<SedeHospitalResponse> responses = service.getAll();

        assertThat(responses).hasSize(1);
        SedeHospitalResponse response = responses.get(0);
        assertThat(response.getSedeId()).isEqualTo(10);
        assertThat(response.getHospitalId()).isEqualTo(5);
        assertThat(response.getSede()).isEqualTo("Central");
        assertThat(response.getFechaCreacion()).isEqualTo(sede.getFechaCreacion());
    }

    @Test
    void getById_devuelveSede() {
        SedeHospital sede = buildSedeHospital();
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.of(sede));

        SedeHospitalResponse response = service.getById(10);

        assertThat(response.getSedeId()).isEqualTo(10);
        assertThat(response.getSede()).isEqualTo("Central");
    }

    @Test
    void getById_lanzaSiNoExiste() {
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(10))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Sede hospital no encontrada con id: 10");
    }

    @Test
    void create_guardaConHospitalExistente() {
        Hospital hospital = new Hospital();
        hospital.setHospitalId(5);
        when(hospitalRepository.findById(5)).thenReturn(Optional.of(hospital));
        when(sedeHospitalRepository.save(any(SedeHospital.class))).thenAnswer(invocation -> {
            SedeHospital saved = invocation.getArgument(0);
            saved.setSedeId(11);
            saved.setHospital(hospital);
            return saved;
        });
        SedeHospitalRequest request = new SedeHospitalRequest();
        request.setSede("Central");
        request.setUbicacion("Av. Siempre Viva 123");
        request.setHospitalId(5);

        SedeHospitalResponse response = service.create(request);

        assertThat(response.getSedeId()).isEqualTo(11);
        assertThat(response.getHospitalId()).isEqualTo(5);
        verify(sedeHospitalRepository).save(any(SedeHospital.class));
    }

    @Test
    void create_lanzaSiHospitalNoExiste() {
        when(hospitalRepository.findById(5)).thenReturn(Optional.empty());
        SedeHospitalRequest request = new SedeHospitalRequest();
        request.setSede("Central");
        request.setHospitalId(5);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Hospital no encontrado con id: 5");
    }

    @Test
    void update_actualizaCamposCuandoExiste() {
        SedeHospital existente = buildSedeHospital();
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.of(existente));
        Hospital nuevoHospital = new Hospital();
        nuevoHospital.setHospitalId(6);
        when(hospitalRepository.findById(6)).thenReturn(Optional.of(nuevoHospital));
        when(sedeHospitalRepository.save(any(SedeHospital.class))).thenAnswer(invocation -> invocation.getArgument(0));
        SedeHospitalRequest request = new SedeHospitalRequest();
        request.setSede("Norte");
        request.setUbicacion("Calle 2");
        request.setHospitalId(6);

        SedeHospitalResponse response = service.update(10, request);

        assertThat(response.getSede()).isEqualTo("Norte");
        assertThat(response.getHospitalId()).isEqualTo(6);
        verify(sedeHospitalRepository).save(any(SedeHospital.class));
    }

    @Test
    void update_lanzaSiSedeNoExiste() {
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.empty());
        SedeHospitalRequest request = new SedeHospitalRequest();
        request.setSede("Norte");
        request.setHospitalId(6);

        assertThatThrownBy(() -> service.update(10, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Sede hospital no encontrada con id: 10");
    }

    @Test
    void update_lanzaSiHospitalNoExiste() {
        SedeHospital existente = buildSedeHospital();
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.of(existente));
        when(hospitalRepository.findById(6)).thenReturn(Optional.empty());
        SedeHospitalRequest request = new SedeHospitalRequest();
        request.setSede("Norte");
        request.setHospitalId(6);

        assertThatThrownBy(() -> service.update(10, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Hospital no encontrado con id: 6");
    }

    @Test
    void delete_eliminaCuandoExiste() {
        SedeHospital existente = buildSedeHospital();
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.of(existente));

        service.delete(10);

        verify(sedeHospitalRepository).delete(existente);
    }

    @Test
    void delete_lanzaSiNoExiste() {
        when(sedeHospitalRepository.findById(10)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(10))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Sede hospital no encontrada con id: 10");
    }

    @Test
    void getByHospital_mapeaResultados() {
        SedeHospital sede = buildSedeHospital();
        when(sedeHospitalRepository.findByHospital_HospitalId(5)).thenReturn(List.of(sede));

        List<SedeHospitalResponse> responses = service.getByHospital(5);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getHospitalId()).isEqualTo(5);
    }

    private SedeHospital buildSedeHospital() {
        Hospital hospital = new Hospital();
        hospital.setHospitalId(5);
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(10);
        sede.setSede("Central");
        sede.setUbicacion("Av. Siempre Viva 123");
        sede.setHospital(hospital);
        sede.setFechaCreacion(LocalDateTime.now());
        sede.setFechaActualizacion(LocalDateTime.now());
        return sede;
    }
}

