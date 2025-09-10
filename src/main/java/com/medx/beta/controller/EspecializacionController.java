package com.medx.beta.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medx.beta.service.EspecializacionService;
import com.medx.beta.model.Especializacion;
import java.util.List;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/especializaciones")
public class EspecializacionController {
    
    @Autowired
    private EspecializacionService especializacionService;

    @GetMapping
    public List<Especializacion> getAllEspecializaciones() {
        return especializacionService.getAllEspecializaciones();
    }

    @GetMapping("/{id}")
    public Especializacion getEspecializacionById(@PathVariable Integer id) {
        return especializacionService.getEspecializacionById(id);
    }

    @PostMapping
    public Especializacion createEspecializacion(@RequestBody Especializacion especializacion) {
        return especializacionService.createEspecializacion(especializacion);
    }

    @PutMapping("/{id}")
    public Especializacion updateEspecializacion(@PathVariable Integer id, @RequestBody Especializacion especializacion) {
        return especializacionService.updateEspecializacion(id, especializacion);
    }

    @DeleteMapping("/{id}")
    public void deleteEspecializacion(@PathVariable Integer id) {
        especializacionService.deleteEspecializacion(id);
    }
}