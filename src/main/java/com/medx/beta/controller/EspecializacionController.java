package com.medx.beta.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medx.beta.service.EspecializacionService;
import com.medx.beta.model.Especializacion;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/especializaciones")
public class EspecializacionController {
    
    @Autowired
    private EspecializacionService especializacionService;

    @GetMapping
    public List<Especializacion> getAllEspecializaciones() { // se mantiene el nombre del método público para no romper clientes
        return especializacionService.getAll();
    }

    @GetMapping("/{id}")
    public Especializacion getEspecializacionById(@PathVariable Integer id) {
        return especializacionService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Especializacion> createEspecializacion(@RequestBody Especializacion especializacion) {
        Especializacion creada = especializacionService.create(especializacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Especializacion> updateEspecializacion(@PathVariable Integer id, @RequestBody Especializacion especializacion) {
        return ResponseEntity.ok(especializacionService.update(id, especializacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEspecializacion(@PathVariable Integer id) {
        especializacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}