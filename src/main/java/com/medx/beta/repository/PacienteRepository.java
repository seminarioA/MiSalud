package com.medx.beta.repository;

import com.medx.beta.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Buscar el Paciente por el id de su Persona asociada (Spring Data resolverá la relación persona.id)
    Optional<Paciente> findByPersonaId(Long personaId);
}
