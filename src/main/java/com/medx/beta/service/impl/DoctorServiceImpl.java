package com.medx.beta.service.impl;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Doctor;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findById(Integer id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado con id: " + id));
    }

    @Override
    public Doctor create(Doctor doctor) {
        doctor.setDoctorId(null); // asegurar creación
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor update(Integer id, Doctor doctor) {
        Doctor existente = findById(id);
        existente.setPrimerNombre(doctor.getPrimerNombre());
        existente.setSegundoNombre(doctor.getSegundoNombre());
        existente.setPrimerApellido(doctor.getPrimerApellido());
        existente.setSegundoApellido(doctor.getSegundoApellido());
        existente.setSedeHospital(doctor.getSedeHospital());
        existente.setEspecializaciones(doctor.getEspecializaciones());
        return doctorRepository.save(existente);
    }

    @Override
    public void delete(Integer id) {
        Doctor existente = findById(id);
        doctorRepository.delete(existente);
    }
}
