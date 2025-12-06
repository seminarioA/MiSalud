package com.medx.beta.controller;

import com.medx.beta.dto.DiagnosticoRequest;
import com.medx.beta.dto.DiagnosticoResponse;
import com.medx.beta.service.DiagnosticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'OPERACIONES')")
    public ResponseEntity<DiagnosticoResponse> create(@Valid @RequestBody DiagnosticoRequest request) {
        return ResponseEntity.ok(diagnosticoService.create(request));
    }

    @GetMapping("/cita/{citaId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PACIENTE', 'RECEPCIONISTA', 'OPERACIONES')")
    public ResponseEntity<List<DiagnosticoResponse>> findByCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(diagnosticoService.findByCitaId(citaId));
    }

    @GetMapping("/historia/{historiaId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'RECEPCIONISTA', 'OPERACIONES')")
    public ResponseEntity<List<DiagnosticoResponse>> findByHistoria(@PathVariable Long historiaId) {
        return ResponseEntity.ok(diagnosticoService.findByHistoriaClinicaId(historiaId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'OPERACIONES')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diagnosticoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
