package com.medx.beta.repository;

import com.medx.beta.model.SedeHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SedeHospitalRepository extends JpaRepository<SedeHospital, Integer> {
}
