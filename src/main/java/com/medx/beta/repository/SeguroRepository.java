package com.medx.beta.repository;

import com.medx.beta.model.Seguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Long> {
    Optional<Seguro> findByNombreAseguradora(String nombre);
}
