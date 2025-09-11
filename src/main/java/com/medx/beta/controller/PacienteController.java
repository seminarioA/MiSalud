package com.medx.beta.controller;

import com.medx.beta.model.Paciente;
import com.medx.beta.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> getAll() {
        return ResponseEntity.ok(pacienteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pacienteService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Paciente> create(@RequestBody Paciente paciente) {
        Paciente creado = pacienteService.create(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> update(@PathVariable Integer id, @RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.update(id, paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

