package com.medx.beta.controller;

import com.medx.beta.dto.HistoriaClinicaRequest;
import com.medx.beta.dto.HistoriaClinicaResponse;
import com.medx.beta.service.HistoriaClinicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historias-clinicas")
@RequiredArgsConstructor
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<HistoriaClinicaResponse>> listar() {
        return ResponseEntity.ok(historiaClinicaService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<HistoriaClinicaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(historiaClinicaService.findById(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<HistoriaClinicaResponse> porPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historiaClinicaService.findByPaciente(pacienteId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR','OPERACIONES')")
    public ResponseEntity<HistoriaClinicaResponse> crear(@Valid @RequestBody HistoriaClinicaRequest request) {
        return ResponseEntity.ok(historiaClinicaService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','OPERACIONES')")
    public ResponseEntity<HistoriaClinicaResponse> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody HistoriaClinicaRequest request) {
        return ResponseEntity.ok(historiaClinicaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        historiaClinicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

