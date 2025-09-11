package com.medx.beta.repository;

import com.medx.beta.model.CitaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Integer> {
    // Buscar citas por id de doctor
    List<CitaMedica> findByDoctor_DoctorId(Integer doctorId);
    // Buscar citas por id de paciente
    List<CitaMedica> findByPaciente_PacienteId(Integer pacienteId);
    // Buscar citas en un rango de fechas
    List<CitaMedica> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
