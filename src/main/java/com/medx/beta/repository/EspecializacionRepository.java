package com.medx.beta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecializacionRepository extends JpaRepository<Especializacion, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndEspecializacionIdNot(String nombre, Integer especializacionId);
}
