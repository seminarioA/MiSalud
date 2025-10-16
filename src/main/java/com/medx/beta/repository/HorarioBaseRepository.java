package com.medx.beta.repository;

import com.medx.beta.model.HorarioBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioBaseRepository extends JpaRepository<HorarioBase, Integer> {
    // Puedes agregar métodos personalizados aquí si es necesario
}
