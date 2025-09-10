package com.medx.beta.service;
import com.medx.beta.model.Especializacion;
import java.util.List;

public interface EspecializacionService {
    // Obtiene una lista de todos los doctores
    List<Especializacion> getAllEspecializaciones();
    // Obtener una especializacion por ID
    Especializacion getEspecializacionById(Integer id);
    //Crear una nueva especializacion
    Especializacion createEspecializacion(Especializacion especializacion);
    //Actualizar una especializacion existente
    Especializacion updateEspecializacion(Integer id, Especializacion especializacion);
    //Eliminar una especializacion por ID
    void deleteEspecializacion(Integer id);
}