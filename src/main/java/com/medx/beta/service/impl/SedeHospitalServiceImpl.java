package com.medx.beta.service.impl;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.SedeHospital;
import com.medx.beta.repository.SedeHospitalRepository;
import com.medx.beta.service.SedeHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SedeHospitalServiceImpl implements SedeHospitalService {

    private final SedeHospitalRepository sedeHospitalRepository;

    @Override
    public List<SedeHospital> getAll() {
        return sedeHospitalRepository.findAll();
    }

    @Override
    public SedeHospital getById(Integer id) {
        return sedeHospitalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sede hospital no encontrada con id: " + id));
    }

    @Override
    public SedeHospital create(SedeHospital sedeHospital) {
        sedeHospital.setSedeId(null); // asegurar creación
        return sedeHospitalRepository.save(sedeHospital);
    }

    @Override
    public SedeHospital update(Integer id, SedeHospital sedeHospital) {
        SedeHospital existente = getById(id);
        existente.setSede(sedeHospital.getSede());
        existente.setUbicacion(sedeHospital.getUbicacion());
        existente.setHospital(sedeHospital.getHospital());
        return sedeHospitalRepository.save(existente);
    }

    @Override
    public void deleteById(Integer id) {
        SedeHospital existente = getById(id);
        sedeHospitalRepository.delete(existente);
    }

    @Override
    public List<SedeHospital> getByHospital(Integer hospitalId) {
        return sedeHospitalRepository.findByHospital_HospitalId(hospitalId);
    }
}
