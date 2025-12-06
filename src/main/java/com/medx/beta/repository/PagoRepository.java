package com.medx.beta.repository;

import com.medx.beta.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    java.util.List<Pago> findByCitaId(Long citaId);
}
