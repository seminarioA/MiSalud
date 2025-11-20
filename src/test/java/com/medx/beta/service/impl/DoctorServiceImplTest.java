package com.medx.beta.service.impl;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.HorarioBase;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.EspecializacionRepository;
import com.medx.beta.repository.HorarioBaseRepository;
import com.medx.beta.repository.SedeHospitalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private SedeHospitalRepository sedeHospitalRepository;
    @Mock
    private HorarioBaseRepository horarioBaseRepository;
    @Mock
    private EspecializacionRepository especializacionRepository;

    @InjectMocks
    private DoctorServiceImpl service;

    @Test
    void getAll_mapsEntities() {
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(3);
        HorarioBase turno = new HorarioBase();
        turno.setTurnoId(4);
        Especializacion esp = new Especializacion();
        esp.setEspecializacionId(8);

        Doctor doctor = new Doctor();
        doctor.setDoctorId(1);
        doctor.setPrimerNombre("Ana");
        doctor.setPrimerApellido("Ruiz");
        doctor.setSedeHospital(sede);
        doctor.setTurno(turno);
        doctor.setEspecializaciones(List.of(esp));
        when(doctorRepository.findAllActivos()).thenReturn(List.of(doctor));

        List<DoctorResponse> out = service.getAll();

        assertThat(out).hasSize(1);
        DoctorResponse dto = out.get(0);
        assertThat(dto.getDoctorId()).isEqualTo(1);
        assertThat(dto.getPrimerNombre()).isEqualTo("Ana");
        assertThat(dto.getSedeId()).isEqualTo(3);
        assertThat(dto.getTurnoId()).isEqualTo(4);
        assertThat(dto.getEspecializacionIds()).containsExactly(8);
    }

    @Test
    void getById_ok() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(5);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(5)).thenReturn(Optional.of(doctor));

        DoctorResponse out = service.getById(5);

        assertThat(out.getDoctorId()).isEqualTo(5);
    }

    @Test
    void getById_notFound() {
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(9)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("9");
    }

    @Test
    void create_ok() {
        DoctorRequest req = buildRequest();
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(1);
        HorarioBase turno = new HorarioBase();
        turno.setTurnoId(2);
        Especializacion esp1 = new Especializacion();
        esp1.setEspecializacionId(10);
        Especializacion esp2 = new Especializacion();
        esp2.setEspecializacionId(20);
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.of(sede));
        when(horarioBaseRepository.findById(2)).thenReturn(Optional.of(turno));
        when(especializacionRepository.findAllById(req.getEspecializacionIds()))
                .thenReturn(List.of(esp1, esp2));
        Doctor saved = new Doctor();
        saved.setDoctorId(7);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(saved);

        DoctorResponse out = service.create(req);

        assertThat(out.getDoctorId()).isEqualTo(7);
        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
        verify(doctorRepository).save(captor.capture());
        Doctor persisted = captor.getValue();
        assertThat(persisted.getPrimerNombre()).isEqualTo("Ana");
        assertThat(persisted.getEspecializaciones()).hasSize(2);
    }

    @Test
    void create_withoutEspecializaciones_skipsLookup() {
        DoctorRequest req = buildRequest();
        req.setEspecializacionIds(null);
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(1);
        HorarioBase turno = new HorarioBase();
        turno.setTurnoId(2);
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.of(sede));
        when(horarioBaseRepository.findById(2)).thenReturn(Optional.of(turno));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        DoctorResponse out = service.create(req);

        assertThat(out.getDoctorId()).isNull();
        verifyNoInteractions(especializacionRepository);
    }

    @Test
    void create_missingSede_throws() {
        DoctorRequest req = buildRequest();
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Sede");
        verify(horarioBaseRepository, never()).findById(any());
    }

    @Test
    void create_missingTurno_throws() {
        DoctorRequest req = buildRequest();
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(1);
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.of(sede));
        when(horarioBaseRepository.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Turno");
    }

    @Test
    void create_missingEspecializacion_throws() {
        DoctorRequest req = buildRequest();
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(1);
        HorarioBase turno = new HorarioBase();
        turno.setTurnoId(2);
        Especializacion esp1 = new Especializacion();
        esp1.setEspecializacionId(10);
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.of(sede));
        when(horarioBaseRepository.findById(2)).thenReturn(Optional.of(turno));
        when(especializacionRepository.findAllById(req.getEspecializacionIds()))
                .thenReturn(List.of(esp1));

        assertThatThrownBy(() -> service.create(req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Especialización");
    }

    @Test
    void update_ok() {
        DoctorRequest req = buildRequest();
        Doctor existente = new Doctor();
        existente.setDoctorId(3);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(3)).thenReturn(Optional.of(existente));
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(1);
        HorarioBase turno = new HorarioBase();
        turno.setTurnoId(2);
        Especializacion esp1 = new Especializacion();
        esp1.setEspecializacionId(10);
        Especializacion esp2 = new Especializacion();
        esp2.setEspecializacionId(20);
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.of(sede));
        when(horarioBaseRepository.findById(2)).thenReturn(Optional.of(turno));
        when(especializacionRepository.findAllById(req.getEspecializacionIds()))
                .thenReturn(List.of(esp1, esp2));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        DoctorResponse out = service.update(3, req);

        assertThat(out.getDoctorId()).isEqualTo(3);
        verify(doctorRepository).save(existente);
        assertThat(existente.getEspecializaciones()).extracting(Especializacion::getEspecializacionId)
                .containsExactlyInAnyOrder(10, 20);
    }

    @Test
    void update_notFound() {
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(15)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(15, buildRequest()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_missingTurno_throws() {
        DoctorRequest req = buildRequest();
        Doctor existente = new Doctor();
        existente.setDoctorId(3);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(3)).thenReturn(Optional.of(existente));
        SedeHospital sede = new SedeHospital();
        sede.setSedeId(1);
        when(sedeHospitalRepository.findById(1)).thenReturn(Optional.of(sede));
        when(horarioBaseRepository.findById(2)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(3, req)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Turno");
    }

    @Test
    void delete_ok() {
        Doctor existente = new Doctor();
        existente.setDoctorId(4);
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(4)).thenReturn(Optional.of(existente));

        service.deleteById(4);

        verify(doctorRepository).desactivarPorId(4);
    }

    @Test
    void delete_notFound() {
        when(doctorRepository.findByDoctorIdAndEstaActivoTrue(44)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(44)).isInstanceOf(NotFoundException.class);
        verify(doctorRepository, never()).desactivarPorId(any());
    }

    private DoctorRequest buildRequest() {
        DoctorRequest req = new DoctorRequest();
        req.setPrimerNombre("Ana");
        req.setSegundoNombre("Maria");
        req.setPrimerApellido("Ruiz");
        req.setSegundoApellido("Lopez");
        req.setSedeId(1);
        req.setTurnoId(2);
        req.setEspecializacionIds(List.of(10, 20));
        return req;
    }
}
