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
        Hospital existente = getById(id); // lanza NotFound si no existe
        existente.setNombre(hospital.getNombre());
        existente.setDescripcion(hospital.getDescripcion());
        return hospitalRepository.save(existente);
    }

    @Override
    public void deleteById(Integer id) {
        Hospital existente = getById(id); // valida existencia
        hospitalRepository.delete(existente);
    }
}