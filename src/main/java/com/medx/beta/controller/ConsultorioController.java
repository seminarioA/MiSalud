package com.medx.beta.controller;

import com.medx.beta.dto.ConsultorioRequest;
import com.medx.beta.dto.ConsultorioResponse;
import com.medx.beta.service.ConsultorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consultorios")
@RequiredArgsConstructor
public class ConsultorioController {

    private final ConsultorioService consultorioService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<ConsultorioResponse>> listar() {
        return ResponseEntity.ok(consultorioService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ConsultorioResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(consultorioService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<ConsultorioResponse> crear(@Valid @RequestBody ConsultorioRequest request) {
        return ResponseEntity.ok(consultorioService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<ConsultorioResponse> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ConsultorioRequest request) {
        return ResponseEntity.ok(consultorioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        consultorioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
