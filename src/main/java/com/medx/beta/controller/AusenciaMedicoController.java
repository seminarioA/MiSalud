package com.medx.beta.controller;

import com.medx.beta.dto.AusenciaMedicoRequest;
import com.medx.beta.dto.AusenciaMedicoResponse;
import com.medx.beta.service.AusenciaMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ausencias")
@RequiredArgsConstructor
public class AusenciaMedicoController {

    private final AusenciaMedicoService ausenciaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'OPERACIONES')")
    public ResponseEntity<AusenciaMedicoResponse> create(@Valid @RequestBody AusenciaMedicoRequest request) {
        return ResponseEntity.ok(ausenciaService.create(request));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'RECEPCIONISTA', 'OPERACIONES')")
    public ResponseEntity<List<AusenciaMedicoResponse>> findByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ausenciaService.findByDoctorId(doctorId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'OPERACIONES')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ausenciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
