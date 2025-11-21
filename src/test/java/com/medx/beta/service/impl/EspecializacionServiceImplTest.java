package com.medx.beta.service.impl;

import com.medx.beta.dto.EspecializacionRequest;
import com.medx.beta.dto.EspecializacionResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Especializacion;
import com.medx.beta.repository.EspecializacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EspecializacionServiceImplTest {

    @Mock
    private EspecializacionRepository especializacionRepository;

    @InjectMocks
    private EspecializacionServiceImpl service;

    @Test
    void getAll_mapsEntities() {
        Especializacion especializacion = new Especializacion();
        especializacion.setEspecializacionId(1);
        especializacion.setNombre("Cardiología");
        especializacion.setDescripcion("Desc");
        when(especializacionRepository.findAll()).thenReturn(List.of(especializacion));

        List<EspecializacionResponse> out = service.getAll();

        assertThat(out).hasSize(1);
        EspecializacionResponse dto = out.get(0);
        assertThat(dto.getEspecializacionId()).isEqualTo(1);
        assertThat(dto.getNombre()).isEqualTo("Cardiología");
        assertThat(dto.getDescripcion()).isEqualTo("Desc");
    }

    @Test
    void getById_ok() {
        Especializacion especializacion = new Especializacion();
        especializacion.setEspecializacionId(5);
        when(especializacionRepository.findById(5)).thenReturn(Optional.of(especializacion));

        EspecializacionResponse out = service.getById(5);

        assertThat(out.getEspecializacionId()).isEqualTo(5);
    }

    @Test
    void getById_notFound() {
        when(especializacionRepository.findById(9)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("9");
    }

    @Test
    void create_ok() {
        EspecializacionRequest req = new EspecializacionRequest();
        req.setNombre("Cardiología");
        req.setDescripcion("Desc");
        Especializacion saved = new Especializacion();
        saved.setEspecializacionId(10);
        when(especializacionRepository.save(any(Especializacion.class))).thenReturn(saved);

        EspecializacionResponse out = service.create(req);

        assertThat(out.getEspecializacionId()).isEqualTo(10);
        verify(especializacionRepository).save(any(Especializacion.class));
    }

    @Test
    void update_ok() {
        Especializacion existente = new Especializacion();
        existente.setEspecializacionId(7);
        when(especializacionRepository.findById(7)).thenReturn(Optional.of(existente));
        when(especializacionRepository.save(any(Especializacion.class))).thenAnswer(inv -> inv.getArgument(0));

        EspecializacionRequest req = new EspecializacionRequest();
        req.setNombre("Actualizado");
        req.setDescripcion("Nueva");

        EspecializacionResponse out = service.update(7, req);

        assertThat(out.getEspecializacionId()).isEqualTo(7);
        assertThat(out.getNombre()).isEqualTo("Actualizado");
        verify(especializacionRepository).save(existente);
    }

    @Test
    void update_notFound() {
        when(especializacionRepository.findById(11)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(11, new EspecializacionRequest()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_ok() {
        Especializacion existente = new Especializacion();
        existente.setEspecializacionId(4);
        when(especializacionRepository.findById(4)).thenReturn(Optional.of(existente));

        service.deleteById(4);

        verify(especializacionRepository).delete(existente);
    }

    @Test
    void delete_notFound() {
        when(especializacionRepository.findById(44)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(44)).isInstanceOf(NotFoundException.class);
        verify(especializacionRepository, never()).delete(any());
    }

    @Test
    void existsByNombre_delegatesToRepository() {
        when(especializacionRepository.existsByNombreIgnoreCase("cardio")).thenReturn(true);

        assertThat(service.existsByNombre("cardio")).isTrue();
    }

    @Test
    void existsByNombreAndNotId_delegatesToRepository() {
        when(especializacionRepository.existsByNombreIgnoreCaseAndEspecializacionIdNot("cardio", 5)).thenReturn(true);

        assertThat(service.existsByNombreAndNotId("cardio", 5)).isTrue();
    }
}
