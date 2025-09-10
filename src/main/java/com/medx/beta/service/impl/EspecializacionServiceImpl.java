package com.medx.beta.service.impl;
import com.medx.beta.service.EspecializacionService;
import com.medx.beta.model.Especializacion;
import com.medx.beta.repository.EspecializacionRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecializacionServiceImpl implements EspecializacionService {
    @Autowired
    private EspecializacionRepository especializacionRepository;

    @Override
    public List<Especializacion> getAllEspecializaciones() {
        return especializacionRepository.findAll();
    }

    @Override
    public Especializacion getEspecializacionById(Integer id) {
        return especializacionRepository.findById(id).orElse(null);
    }

    @Override
    public Especializacion createEspecializacion(Especializacion especializacion) {
        return especializacionRepository.save(especializacion);
    }

    @Override
    public Especializacion updateEspecializacion(Integer id, Especializacion especializacion) {
        Especializacion existente = especializacionRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(especializacion.getNombre());
            existente.setDescripcion(especializacion.getDescripcion());
            return especializacionRepository.save(existente);
        }
        return null;
    }

    @Override
    public void deleteEspecializacion(Integer id) {
        especializacionRepository.deleteById(id);
    }
}