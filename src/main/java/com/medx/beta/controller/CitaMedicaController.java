package com.medx.beta.controller;

import com.medx.beta.model.CitaMedica;
import com.medx.beta.service.CitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    @GetMapping
    public ResponseEntity<List<CitaMedica>> getAll() {
        return ResponseEntity.ok(citaMedicaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(citaMedicaService.getById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<CitaMedica>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(citaMedicaService.getByDoctor(doctorId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaMedica>> getByPaciente(@PathVariable Integer pacienteId) {
        return ResponseEntity.ok(citaMedicaService.getByPaciente(pacienteId));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<CitaMedica>> getByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(citaMedicaService.getByFechaBetween(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<CitaMedica> create(@RequestBody CitaMedica citaMedica) {
        CitaMedica creada = citaMedicaService.create(citaMedica);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> update(@PathVariable Integer id, @RequestBody CitaMedica citaMedica) {
        return ResponseEntity.ok(citaMedicaService.update(id, citaMedica));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        citaMedicaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

