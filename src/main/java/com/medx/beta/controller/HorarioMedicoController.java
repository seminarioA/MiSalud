package com.medx.beta.controller;

import com.medx.beta.dto.HorarioMedicoRequest;
import com.medx.beta.dto.HorarioMedicoResponse;
import com.medx.beta.service.HorarioMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/horarios-medicos")
@RequiredArgsConstructor
public class HorarioMedicoController {

    private final HorarioMedicoService horarioMedicoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<HorarioMedicoResponse>> listar() {
        return ResponseEntity.ok(horarioMedicoService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<HorarioMedicoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(horarioMedicoService.findById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<HorarioMedicoResponse>> porDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(horarioMedicoService.findByDoctor(doctorId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR','OPERACIONES')")
    public ResponseEntity<HorarioMedicoResponse> crear(@Valid @RequestBody HorarioMedicoRequest request) {
        return ResponseEntity.ok(horarioMedicoService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','OPERACIONES')")
    public ResponseEntity<HorarioMedicoResponse> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody HorarioMedicoRequest request) {
        return ResponseEntity.ok(horarioMedicoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioMedicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
