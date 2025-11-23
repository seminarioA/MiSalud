package com.medx.beta.service.impl;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;
import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Doctor;
import com.medx.beta.model.DoctorEspecialidad;
import com.medx.beta.model.DoctorEspecialidadId;
import com.medx.beta.model.Especialidad;
import com.medx.beta.model.Persona;
import com.medx.beta.repository.DoctorEspecialidadRepository;
import com.medx.beta.repository.DoctorRepository;
import com.medx.beta.repository.EspecialidadRepository;
import com.medx.beta.repository.PersonaRepository;
import com.medx.beta.service.DoctorService;
import com.medx.beta.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PersonaRepository personaRepository;
    private final EspecialidadRepository especialidadRepository;
    private final DoctorEspecialidadRepository doctorEspecialidadRepository;
    private final PersonService personService;

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse findById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        return toResponse(doctor);
    }

    @Override
    public DoctorResponse create(DoctorRequest request) {
        Persona persona = new Persona();
        applyPersona(persona, request.persona());
        personaRepository.save(persona);
        Doctor doctor = new Doctor();
        doctor.setPersona(persona);
        doctor.setNumeroColegiatura(request.numeroColegiatura());
        Doctor saved = doctorRepository.save(doctor);
        syncEspecialidades(saved, request.especialidadIds());
        return toResponse(saved);
    }

    @Override
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        Persona persona = doctor.getPersona();
        applyPersona(persona, request.persona());
        personaRepository.save(persona);
        doctor.setNumeroColegiatura(request.numeroColegiatura());
        Doctor saved = doctorRepository.save(doctor);
        syncEspecialidades(saved, request.especialidadIds());
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor no encontrado"));
        doctorEspecialidadRepository.deleteAll(
                doctorEspecialidadRepository.findAll().stream()
                        .filter(de -> de.getDoctor().equals(doctor))
                        .toList()
        );
        doctorRepository.delete(doctor);
    }

    private void syncEspecialidades(Doctor doctor, List<Long> especialidadIds) {
        doctorEspecialidadRepository.deleteAll(
                doctorEspecialidadRepository.findAll().stream()
                        .filter(de -> de.getDoctor().getId().equals(doctor.getId()))
                        .toList()
        );
        if (especialidadIds == null || especialidadIds.isEmpty()) {
            return;
        }
        Set<Especialidad> especialidades = especialidadRepository.findAllById(especialidadIds).stream().collect(Collectors.toSet());
        if (especialidades.size() != especialidadIds.size()) {
            throw new NotFoundException("Alguna especialidad no existe");
        }
        especialidades.forEach(especialidad -> {
            DoctorEspecialidadId id = new DoctorEspecialidadId(doctor.getId(), especialidad.getId());
            DoctorEspecialidad enlace = new DoctorEspecialidad();
            enlace.setId(id);
            enlace.setDoctor(doctor);
            enlace.setEspecialidad(especialidad);
            doctorEspecialidadRepository.save(enlace);
        });
    }

    private DoctorResponse toResponse(Doctor doctor) {
        PersonaResponse persona = personService.findById(doctor.getPersona().getId());
        List<Long> especialidadIds = doctorEspecialidadRepository.findAll().stream()
                .filter(de -> de.getDoctor().getId().equals(doctor.getId()))
                .map(de -> de.getEspecialidad().getId())
                .toList();
        return new DoctorResponse(
                doctor.getId(),
                persona,
                doctor.getNumeroColegiatura(),
                especialidadIds
        );
    }

    private void applyPersona(Persona persona, PersonaRequest request) {
        persona.setPrimerNombre(request.primerNombre());
        persona.setSegundoNombre(request.segundoNombre());
        persona.setPrimerApellido(request.primerApellido());
        persona.setSegundoApellido(request.segundoApellido());
        persona.setTipoDocumento(request.tipoDocumento());
        persona.setNumeroDocumento(request.numeroDocumento());
        persona.setFechaNacimiento(request.fechaNacimiento());
        persona.setGenero(request.genero());
        persona.setNumeroTelefono(request.numeroTelefono());
        persona.setUrlFotoPerfil(request.urlFotoPerfil());
    }
}
