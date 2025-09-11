package com.medx.beta.repository;

import com.medx.beta.model.SedeHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SedeHospitalRepository extends JpaRepository<SedeHospital, Integer> {
    // Listar sedes por hospital
    List<SedeHospital> findByHospital_HospitalId(Integer hospitalId);
}
