package com.medx.beta.repository;

import com.medx.beta.model.DoctorEspecialidad;
import com.medx.beta.model.DoctorEspecialidadId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorEspecialidadRepository extends JpaRepository<DoctorEspecialidad, DoctorEspecialidadId> {
}

