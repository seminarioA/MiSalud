package com.medx.beta.repository;

import com.medx.beta.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    boolean existsByDoctorIdAndFechaCitaAndHoraCitaAndEstadoNot(
            Long doctorId,
            java.time.LocalDate fecha,
            java.time.LocalTime hora,
            Cita.EstadoCita estado);

    java.util.List<Cita> findByDoctorIdAndFechaCita(Long doctorId, java.time.LocalDate fecha);

    java.util.List<Cita> findByPacienteId(Long pacienteId);
}
