package com.medx.beta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.medx.beta.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT d FROM Doctor d WHERE d.estaActivo = true ORDER BY d.primerApellido, d.primerNombre")
    List<Doctor> findAllActivos();

    @Query("SELECT d FROM Doctor d WHERE d.estaActivo = true AND d.sedeHospital.sedeId = :sedeId ORDER BY d.primerApellido, d.primerNombre")
    List<Doctor> findActivosPorSede(@Param("sedeId") Integer sedeId);

    Optional<Doctor> findByDoctorIdAndEstaActivoTrue(Integer doctorId);

    @Modifying
    @Transactional
    @Query("UPDATE Doctor d SET d.estaActivo = false WHERE d.doctorId = :doctorId")
    int desactivarPorId(@Param("doctorId") Integer doctorId);
}
