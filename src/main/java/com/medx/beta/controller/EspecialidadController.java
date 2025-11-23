package com.medx.beta.controller;

import com.medx.beta.dto.EspecialidadRequest;
import com.medx.beta.dto.EspecialidadResponse;
import com.medx.beta.service.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<EspecialidadResponse>> listar() {
        return ResponseEntity.ok(especialidadService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EspecialidadResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<EspecialidadResponse> crear(@Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.ok(especialidadService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<EspecialidadResponse> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody EspecialidadRequest request) {
        return ResponseEntity.ok(especialidadService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
