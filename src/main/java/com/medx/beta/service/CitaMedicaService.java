package com.medx.beta.service;

import com.medx.beta.model.CitaMedica;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaMedicaService {
    List<CitaMedica> getAll();
    CitaMedica getById(Integer id);
    CitaMedica create(CitaMedica citaMedica);
    CitaMedica update(Integer id, CitaMedica citaMedica);
    void deleteById(Integer id);

    // Consultas adicionales
    List<CitaMedica> getByDoctor(Integer doctorId);
    List<CitaMedica> getByPaciente(Integer pacienteId);
    List<CitaMedica> getByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
