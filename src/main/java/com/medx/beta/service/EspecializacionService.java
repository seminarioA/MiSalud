package com.medx.beta.service;
import com.medx.beta.dto.EspecializacionRequest;
import com.medx.beta.dto.EspecializacionResponse;
import com.medx.beta.model.Especializacion;
import java.util.List;

public interface EspecializacionService {
    // Obtiene una lista de todos los doctores
    List<EspecializacionResponse> getAll();
    // Obtener una especializacion por ID
    EspecializacionResponse getById(Integer id);
    //Crear una nueva especializacion
    EspecializacionResponse create(EspecializacionRequest especializacionRequest);
    //Actualizar una especializacion existente
    EspecializacionResponse update(Integer id, EspecializacionRequest especializacionRequest);
    //Eliminar una especializacion por ID
    void deleteById(Integer id);
}