package com.medx.beta.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medx.beta.service.HospitalService;
import com.medx.beta.model.Hospital;
import com.medx.beta.repository.HospitalRepository;
import com.medx.beta.exception.NotFoundException;

import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {
    
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital getById(Integer id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + id));
    }

    @Override
    public Hospital create(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @Override
    public Hospital update(Integer id, Hospital hospital) { 
        Hospital existente = hospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + id));
        existente.setNombre(hospital.getNombre());
        existente.setDescripcion(hospital.getDescripcion());
        // No actualizamos sedes directamente aqui para evitar desincronizacion
        return hospitalRepository.save(existente);
    }

    @Override
    public void deleteById(Integer id) {
        Hospital existente = hospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hospital no encontrado con id: " + id));
        hospitalRepository.delete(existente);
    }

}