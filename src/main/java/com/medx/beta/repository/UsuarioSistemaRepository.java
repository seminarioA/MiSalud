package com.medx.beta.repository;

import com.medx.beta.model.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {

    Optional<UsuarioSistema> findByEmail(String email);
}

