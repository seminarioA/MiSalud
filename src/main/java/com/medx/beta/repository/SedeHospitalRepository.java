package com.medx.beta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medx.beta.model.SedeHospital;

@Repository
public interface SedeHospitalRepository extends JpaRepository<SedeHospital, Integer> {
    // Listar sedes por hospital
    List<SedeHospital> findByHospital_HospitalId(Integer hospitalId);
}
