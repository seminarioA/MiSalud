package com.medx.beta.service;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Especializacion;
import com.medx.beta.repository.EspecializacionRepository;
import com.medx.beta.service.impl.EspecializacionServiceImpl;
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
class EspecializacionServiceImplTest {

    @Mock
    private EspecializacionRepository repository;

    @InjectMocks
    private EspecializacionServiceImpl service;

    private Especializacion especializacion;

    @BeforeEach
    void setUp() {
        especializacion = new Especializacion();
        especializacion.setEspecializacionId(1);
        especializacion.setNombre("Cardiologia");
        especializacion.setDescripcion("Corazon");
    }

    @Test
    @DisplayName("getAll retorna lista")
    void getAll_ok() {
        when(repository.findAll()).thenReturn(List.of(especializacion));
        assertThat(service.getAll()).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("getById ok")
    void getById_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(especializacion));
        Especializacion found = service.getById(1);
        assertThat(found.getNombre()).isEqualTo("Cardiologia");
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
        Especializacion nueva = new Especializacion();
        nueva.setEspecializacionId(888);
        nueva.setNombre("Neuro");
        when(repository.save(any(Especializacion.class))).thenAnswer(a -> { Especializacion e = a.getArgument(0); e.setEspecializacionId(10); return e;});
        Especializacion creada = service.create(nueva);
        assertThat(creada.getEspecializacionId()).isEqualTo(10);
        ArgumentCaptor<Especializacion> captor = ArgumentCaptor.forClass(Especializacion.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getEspecializacionId()).isNull();
    }

    @Test
    @DisplayName("update modifica campos")
    void update_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(especializacion));
        when(repository.save(any(Especializacion.class))).thenAnswer(a -> a.getArgument(0));
        Especializacion cambios = new Especializacion();
        cambios.setNombre("Cardio");
        cambios.setDescripcion("Nueva desc");
        Especializacion updated = service.update(1, cambios);
        assertThat(updated.getNombre()).isEqualTo("Cardio");
        assertThat(updated.getDescripcion()).contains("Nueva");
    }

    @Test
    @DisplayName("update NotFound")
    void update_notFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(2, especializacion))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("deleteById ok")
    void delete_ok() {
        when(repository.findById(1)).thenReturn(Optional.of(especializacion));
        service.deleteById(1);
        verify(repository).delete(especializacion);
    }

    @Test
    @DisplayName("deleteById NotFound")
    void delete_notFound() {
        when(repository.findById(5)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteById(5))
                .isInstanceOf(NotFoundException.class);
    }
}

