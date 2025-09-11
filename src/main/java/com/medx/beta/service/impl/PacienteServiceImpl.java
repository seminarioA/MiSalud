package com.medx.beta.service.impl;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Paciente;
import com.medx.beta.repository.PacienteRepository;
import com.medx.beta.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Override
    public List<Paciente> getAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente getById(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado con id: " + id));
    }

    @Override
    public Paciente create(Paciente paciente) {
        paciente.setPacienteId(null); // asegurar creación
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente update(Integer id, Paciente paciente) {
        Paciente existente = getById(id);
        existente.setPrimerNombre(paciente.getPrimerNombre());
        existente.setSegundoNombre(paciente.getSegundoNombre());
        existente.setPrimerApellido(paciente.getPrimerApellido());
        existente.setSegundoApellido(paciente.getSegundoApellido());
        existente.setFechaNacimiento(paciente.getFechaNacimiento());
        existente.setDomicilio(paciente.getDomicilio());
        existente.setEstaActivo(paciente.getEstaActivo());
        return pacienteRepository.save(existente);
    }

    @Override
    public void deleteById(Integer id) {
        Paciente existente = getById(id);
        pacienteRepository.delete(existente);
    }
}
