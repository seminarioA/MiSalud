package com.medx.beta.repository;

import com.medx.beta.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    Optional<HistoriaClinica> findByPacienteId(Long pacienteId);
}
