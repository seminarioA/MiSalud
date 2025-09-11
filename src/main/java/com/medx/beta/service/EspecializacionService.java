package com.medx.beta.service;
import com.medx.beta.model.Especializacion;
import java.util.List;

public interface EspecializacionService {
    // Obtiene una lista de todos los doctores
    List<Especializacion> getAll();
    // Obtener una especializacion por ID
    Especializacion getById(Integer id);
    //Crear una nueva especializacion
    Especializacion create(Especializacion especializacion);
    //Actualizar una especializacion existente
    Especializacion update(Integer id, Especializacion especializacion);
    //Eliminar una especializacion por ID
    void deleteById(Integer id);
}