package com.medx.beta.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medx.beta.service.HospitalService;
import com.medx.beta.model.Hospital;
import com.medx.beta.repository.HospitalRepository;

import java.util.List;

@Service
public class HospitalServiceImpl implements HospitalService {
    
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public List<Hospital> getAllHospitales() {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital getHospitalById(Integer id) {
        return hospitalRepository.findById(id).orElse(null);
    }

    @Override
    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    @Override
    public Hospital updateHospital(Integer id, Hospital hospital) { 
        Hospital existente = hospitalRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(hospital.getNombre());
            existente.setDescripcion(hospital.getDescripcion());
            return hospitalRepository.save(existente);
        }
        return null;
    }

    @Override
    public void deleteHospital(Integer id) {
        hospitalRepository.deleteById(id);
    }
}