package com.medx.beta.repository;

import com.medx.beta.model.AusenciaMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AusenciaMedicoRepository extends JpaRepository<AusenciaMedico, Long> {

    List<AusenciaMedico> findByDoctorId(Long doctorId);

    @Query("SELECT a FROM AusenciaMedico a WHERE a.doctor.id = :doctorId AND " +
            "((:fecha BETWEEN a.fechaInicio AND a.fechaFin))")
    List<AusenciaMedico> findByDoctorAndDate(@Param("doctorId") Long doctorId, @Param("fecha") LocalDate fecha);
}
