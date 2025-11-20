package com.medx.beta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.medx.beta.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    @Query("SELECT p FROM Paciente p WHERE p.estaActivo = true ORDER BY p.primerApellido, p.primerNombre")
    List<Paciente> findAllActivos();

    @Query("SELECT p FROM Paciente p WHERE p.estaActivo = true AND (LOWER(p.primerNombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR LOWER(p.primerApellido) LIKE LOWER(CONCAT('%', :termino, '%'))) ORDER BY p.primerApellido, p.primerNombre")
    List<Paciente> buscarActivosPorNombre(@Param("termino") String termino);

    @Modifying
    @Transactional
    @Query("UPDATE Paciente p SET p.estaActivo = false WHERE p.pacienteId = :pacienteId")
    int desactivarPorId(@Param("pacienteId") Integer pacienteId);

    Optional<Paciente> findByPacienteIdAndEstaActivoTrue(Integer pacienteId);
}
