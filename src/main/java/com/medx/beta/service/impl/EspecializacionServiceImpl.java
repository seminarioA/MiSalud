package com.medx.beta.service.impl;

import com.medx.beta.service.EspecializacionService;
import com.medx.beta.model.Especializacion;
import com.medx.beta.repository.EspecializacionRepository;
import com.medx.beta.exception.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {
    @Autowired
    private EspecializacionRepository especializacionRepository;

    @Override
    public List<Especializacion> getAll() {
        return especializacionRepository.findAll();
    }

    @Override
    public Especializacion getById(Integer id) {
        return especializacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Especialización no encontrada con ID: " + id));
    }

    @Override
    public Especializacion create(Especializacion especializacion) {
        especializacion.setEspecializacionId(null); // asegurar creación si existe ID
        return especializacionRepository.save(especializacion);
    }

    @Override
    public Especializacion update(Integer id, Especializacion especializacion) {
        Especializacion existente = especializacionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Especialización no encontrada con ID: " + id));
        
        existente.setNombre(especializacion.getNombre());
        existente.setDescripcion(especializacion.getDescripcion());
        return especializacionRepository.save(existente);
    }

    @Override
    public void deleteEspecializacion(Integer id) {
        especializacionRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByNombre(String nombre) {
        return especializacionRepository.existsByNombreIgnoreCase(nombre);
    }
    
    @Override
    public boolean existsByNombreAndNotId(String nombre, Integer especializacionId) {
        return especializacionRepository.existsByNombreIgnoreCaseAndEspecializacionIdNot(nombre, especializacionId);
    }
}