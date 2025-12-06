package com.medx.beta.repository;

import com.medx.beta.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {
    List<Diagnostico> findByCitaId(Long citaId);

    List<Diagnostico> findByHistoriaClinicaId(Long historiaClinicaId);
}
