package com.medx.beta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medx.beta.model.Hospital;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    // Buscar hospitales por nombre
    List<Hospital> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe un hospital con el nombre exacto (ignorando mayúsculas/minúsculas)
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Verificar si existe otro hospital con el mismo nombre (excluyendo el hospital con el ID dado)
    boolean existsByNombreIgnoreCaseAndHospitalIdNot(String nombre, Integer hospitalId);
}
