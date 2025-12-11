package com.medx.beta.repository;

import com.medx.beta.model.DoctorEspecialidad;
import com.medx.beta.model.DoctorEspecialidadId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DoctorEspecialidadRepository extends JpaRepository<DoctorEspecialidad, DoctorEspecialidadId> {

    @Query("SELECT de.especialidad.id FROM DoctorEspecialidad de WHERE de.doctor.id = :doctorId")
    List<Long> findEspecialidadIdsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT de.especialidad.nombre FROM DoctorEspecialidad de WHERE de.doctor.id = :doctorId")
    List<String> findEspecialidadNombresByDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DoctorEspecialidad de WHERE de.doctor.id = :doctorId")
    void deleteByDoctorId(@Param("doctorId") Long doctorId);
}
