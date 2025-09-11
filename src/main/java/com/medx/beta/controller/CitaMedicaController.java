package com.medx.beta.controller;

import com.medx.beta.model.CitaMedica;
import com.medx.beta.service.CitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Validated
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    @GetMapping
    public ResponseEntity<List<CitaMedica>> getAll() {
        return ResponseEntity.ok(citaMedicaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaMedica> getById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(citaMedicaService.getById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<CitaMedica>> getByDoctor(@PathVariable @Positive(message = "El id debe ser positivo") Integer doctorId) {
        return ResponseEntity.ok(citaMedicaService.getByDoctor(doctorId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaMedica>> getByPaciente(@PathVariable @Positive(message = "El id debe ser positivo") Integer pacienteId) {
        return ResponseEntity.ok(citaMedicaService.getByPaciente(pacienteId));
    }

    @GetMapping("/rango")
    public ResponseEntity<List<CitaMedica>> getByRangoFechas(
            @RequestParam @NotNull(message = "inicio es obligatorio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @NotNull(message = "fin es obligatorio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("El parámetro 'inicio' no puede ser posterior a 'fin'");
        }
        return ResponseEntity.ok(citaMedicaService.getByFechaBetween(inicio, fin));
    }

    @PostMapping
    public ResponseEntity<CitaMedica> create(@RequestBody @Valid CitaMedica citaMedica) {
        CitaMedica creada = citaMedicaService.create(citaMedica);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> update(@PathVariable @Positive(message = "El id debe ser positivo") Integer id, @RequestBody @Valid CitaMedica citaMedica) {
        return ResponseEntity.ok(citaMedicaService.update(id, citaMedica));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        citaMedicaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
