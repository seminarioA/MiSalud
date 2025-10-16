package com.medx.beta.controller;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
@Validated
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> getAll() {
        return ResponseEntity.ok(pacienteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> getById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(pacienteService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> create(@RequestBody @Valid PacienteRequest paciente) {
        PacienteResponse creado = pacienteService.create(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> update(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                           @RequestBody @Valid PacienteRequest paciente) {
        return ResponseEntity.ok(pacienteService.update(id, paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        pacienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
