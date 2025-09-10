package com.medx.beta.repository;

import org.springframework.stereotype.Repository;
import com.medx.beta.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
