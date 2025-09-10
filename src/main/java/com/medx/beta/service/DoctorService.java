package com.medx.beta.service;

import com.medx.beta.model.Doctor;
import java.util.List;

public interface DoctorService {
    // Obtiene todos los doctores
    List<Doctor> findAll();
    // Busca un doctor por ID (lanza excepción si no existe)
    Doctor findById(Integer id);
    // Crea un nuevo doctor
    Doctor create(Doctor doctor);
    // Actualiza un doctor existente
    Doctor update(Integer id, Doctor doctor);
    // Elimina un doctor por ID
    void delete(Integer id);
}
