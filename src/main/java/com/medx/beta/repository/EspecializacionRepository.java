package com.medx.beta.repository;

import com.medx.beta.model.Especializacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecializacionRepository extends JpaRepository<Especializacion, Integer> {
}
