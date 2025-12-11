package com.medx.beta.controller;

import com.medx.beta.dto.CitaRequest;
import com.medx.beta.dto.CitaResponse;
import com.medx.beta.service.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<CitaResponse>> listar() {
        return ResponseEntity.ok(citaService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<CitaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.findById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<CitaResponse>> porDoctor(@PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.findByDoctor(doctorId, fecha));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<CitaResponse>> porPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaService.findByPaciente(pacienteId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<CitaResponse> crear(@Valid @RequestBody CitaRequest request) {
        return ResponseEntity.ok(citaService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<CitaResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody CitaRequest request) {
        return ResponseEntity.ok(citaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE','OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reprogramacion")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA', 'OPERACIONES')")
    public ResponseEntity<CitaResponse> reprogramar(@PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) java.time.LocalTime hora) {
        return ResponseEntity.ok(citaService.reprogramar(id, fecha, hora));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('DOCTOR', 'RECEPCIONISTA', 'OPERACIONES')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id,
            @RequestParam com.medx.beta.model.Cita.EstadoCita nuevoEstado) {
        citaService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mis-citas")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<CitaResponse>> misCitas() {
        return ResponseEntity.ok(citaService.findMine());
    }
}
