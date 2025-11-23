package com.medx.beta.controller;

import com.medx.beta.dto.ResponsablePacienteRequest;
import com.medx.beta.dto.ResponsablePacienteResponse;
import com.medx.beta.service.ResponsablePacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes/{pacienteId}/responsables")
@RequiredArgsConstructor
public class ResponsablePacienteController {

    private final ResponsablePacienteService responsablePacienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<ResponsablePacienteResponse>> listar(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(responsablePacienteService.findByPaciente(pacienteId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<ResponsablePacienteResponse> crear(@PathVariable Long pacienteId,
                                                             @Valid @RequestBody ResponsablePacienteRequest request) {
        ResponsablePacienteRequest enriched = new ResponsablePacienteRequest(pacienteId, request.personaResponsableId(), request.relacion());
        return ResponseEntity.ok(responsablePacienteService.create(enriched));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<ResponsablePacienteResponse> actualizar(@PathVariable Long pacienteId,
                                                                  @PathVariable Long id,
                                                                  @Valid @RequestBody ResponsablePacienteRequest request) {
        ResponsablePacienteRequest enriched = new ResponsablePacienteRequest(pacienteId, request.personaResponsableId(), request.relacion());
        return ResponseEntity.ok(responsablePacienteService.update(id, enriched));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        responsablePacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

