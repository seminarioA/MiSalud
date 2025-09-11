package com.medx.beta.repository;

import com.medx.beta.model.Especializacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecializacionRepository extends JpaRepository<Especializacion, Integer> {
    // Verificar si existe una especialización con el nombre exacto (ignorando mayúsculas/minúsculas)
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Verificar si existe otra especialización con el mismo nombre (excluyendo la especialización con el ID dado)
    boolean existsByNombreIgnoreCaseAndEspecializacionIdNot(String nombre, Integer especializacionId);
}
