package com.medx.beta.controller;

import com.medx.beta.dto.TratamientoRequest;
import com.medx.beta.dto.TratamientoResponse;
import com.medx.beta.service.TratamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tratamientos")
@RequiredArgsConstructor
public class TratamientoController {

    private final TratamientoService tratamientoService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'OPERACIONES')")
    public ResponseEntity<TratamientoResponse> create(@Valid @RequestBody TratamientoRequest request) {
        return ResponseEntity.ok(tratamientoService.create(request));
    }

    @GetMapping("/diagnostico/{diagnosticoId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PACIENTE', 'RECEPCIONISTA', 'OPERACIONES')")
    public ResponseEntity<List<TratamientoResponse>> findByDiagnostico(@PathVariable Long diagnosticoId) {
        return ResponseEntity.ok(tratamientoService.findByDiagnosticoId(diagnosticoId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'OPERACIONES')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tratamientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
