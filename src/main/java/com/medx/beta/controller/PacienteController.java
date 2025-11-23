package com.medx.beta.controller;

import com.medx.beta.dto.PacienteRequest;
import com.medx.beta.dto.PacienteResponse;
import com.medx.beta.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<PacienteResponse>> listar() {
        return ResponseEntity.ok(pacienteService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PacienteResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PacienteResponse> crear(@Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(pacienteService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PacienteResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody PacienteRequest request) {
        return ResponseEntity.ok(pacienteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
