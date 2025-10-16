package com.medx.beta.service;
import com.medx.beta.dto.HospitalRequest;
import com.medx.beta.dto.HospitalResponse;
import com.medx.beta.model.Hospital;
import java.util.List;

public interface HospitalService {
    //Obtener una lista de todos los hospitales 
    List<HospitalResponse> getAll();
    //Obtener un hospital por ID
    HospitalResponse getById(Integer id);
    //Registrar un nuevo hospital
    HospitalResponse create(HospitalRequest hospitalRequest);
    //Actualizar un hospital existente
    HospitalResponse update(Integer id, HospitalRequest hospitalRequest);
    //Eliminar un hospital mediante su ID
    void deleteById(Integer id);
}