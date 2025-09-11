package com.medx.beta.service;
import com.medx.beta.model.Hospital;
import java.util.List;

public interface HospitalService {
    //Obtener una lista de todos los hospitales 
    List<Hospital> getAll();
    //Obtener un hospital por ID
    Hospital getById(Integer id);
    //Registrar un nuevo hospital
    Hospital create(Hospital hospital);
    //Actualizar un hospital existente
    Hospital update(Integer id, Hospital hospital);
    //Eliminar un hospital mediante su ID
    void deleteById(Integer id);
}