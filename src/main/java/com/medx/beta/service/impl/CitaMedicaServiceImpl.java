package com.medx.beta.service.impl;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.CitaMedica;
import com.medx.beta.repository.CitaMedicaRepository;
import com.medx.beta.service.CitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaMedicaServiceImpl implements CitaMedicaService {

    private final CitaMedicaRepository citaMedicaRepository;

    @Override
    public List<CitaMedica> getAll() {
        return citaMedicaRepository.findAll();
    }

    @Override
    public CitaMedica getById(Integer id) {
        return citaMedicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita médica no encontrada con id: " + id));
    }

    @Override
    public CitaMedica create(CitaMedica citaMedica) {
        citaMedica.setCitaId(null); // asegurar creación
        return citaMedicaRepository.save(citaMedica);
    }

    @Override
    public CitaMedica update(Integer id, CitaMedica citaMedica) {
        CitaMedica existente = getById(id);
        existente.setFecha(citaMedica.getFecha());
        existente.setCosto(citaMedica.getCosto());
        existente.setDoctor(citaMedica.getDoctor());
        existente.setPaciente(citaMedica.getPaciente());
        return citaMedicaRepository.save(existente);
    }

    @Override
    public void deleteById(Integer id) {
        CitaMedica existente = getById(id);
        citaMedicaRepository.delete(existente);
    }

    @Override
    public List<CitaMedica> getByDoctor(Integer doctorId) {
        return citaMedicaRepository.findByDoctor_DoctorId(doctorId);
    }

    @Override
    public List<CitaMedica> getByPaciente(Integer pacienteId) {
        return citaMedicaRepository.findByPaciente_PacienteId(pacienteId);
    }

    @Override
    public List<CitaMedica> getByFechaBetween(LocalDateTime inicio, LocalDateTime fin) {
        return citaMedicaRepository.findByFechaBetween(inicio, fin);
    }
}
