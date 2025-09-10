package com.medx.beta.service;
import com.medx.beta.model.Hospital;
import java.util.List;

public interface HospitalService {
    //Obtener una lista de todos los hospitales 
    List<Hospital> getAllHospitales();
    //Obtener un hospital por ID
    Hospital getHospitalById(Integer id);
    //Registrar un nuevo hospital
    Hospital createHospital(Hospital hospital);
    //Actualizar un hospital existente
    Hospital updateHospital(Integer id, Hospital hospital);
    //Eliminar un hospital mediante su ID
    void deleteHospital(Integer id);
}