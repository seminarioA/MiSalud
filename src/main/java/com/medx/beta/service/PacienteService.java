package com.medx.beta.service;

import com.medx.beta.model.Paciente;
import java.util.List;

public interface PacienteService {
    // Lista todos los pacientes
    List<Paciente> findAll();
    // Busca un paciente por ID
    Paciente findById(Integer id);
    // Crea un nuevo paciente
    Paciente create(Paciente paciente);
    // Actualiza un paciente existente
    Paciente update(Integer id, Paciente paciente);
    // Elimina (hard delete) un paciente por ID
    void delete(Integer id);
}

