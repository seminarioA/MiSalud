package com.medx.beta.controller;

import com.medx.beta.dto.CitaMedicaRequest;
import com.medx.beta.dto.CitaMedicaResponse;
import com.medx.beta.service.CitaMedicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
@Validated
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA')")
    public ResponseEntity<List<CitaMedicaResponse>> getAll() {
        return ResponseEntity.ok(citaMedicaService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA')")
    public ResponseEntity<CitaMedicaResponse> getById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(citaMedicaService.getById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA')")
    public ResponseEntity<List<CitaMedicaResponse>> getByDoctor(@PathVariable @Positive(message = "El id debe ser positivo") Integer doctorId) {
        return ResponseEntity.ok(citaMedicaService.getByDoctor(doctorId));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA', 'PACIENTE')")
    public ResponseEntity<List<CitaMedicaResponse>> getByPaciente(@PathVariable @Positive(message = "El id debe ser positivo") Integer pacienteId) {
        return ResponseEntity.ok(citaMedicaService.getByPaciente(pacienteId));
    }

    @GetMapping("/rango")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA')")
    public ResponseEntity<List<CitaMedicaResponse>> getByRangoFechas(
            @RequestParam @NotNull(message = "inicio es obligatorio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @NotNull(message = "fin es obligatorio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        return ResponseEntity.ok(citaMedicaService.getByFechaBetween(inicio, fin));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<CitaMedicaResponse> create(@RequestBody @Valid CitaMedicaRequest citaMedicaRequest) {
        CitaMedicaResponse creada = citaMedicaService.create(citaMedicaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<CitaMedicaResponse> update(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                           @RequestBody @Valid CitaMedicaRequest citaMedicaRequest) {
        return ResponseEntity.ok(citaMedicaService.update(id, citaMedicaRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        citaMedicaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
