package com.medx.beta.service;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import java.util.List;

public interface DoctorService {
    // Obtiene todos los doctores
    List<DoctorResponse> getAll();
    // Busca un doctor por ID (lanza excepción si no existe)
    DoctorResponse getById(Integer id);
    // Crea un nuevo doctor
    DoctorResponse create(DoctorRequest doctorRequest);
    // Actualiza un doctor existente
    DoctorResponse update(Integer id, DoctorRequest doctorRequest);
    // Elimina un doctor por ID
    void deleteById(Integer id);
}
