package com.medx.beta.controller;

import com.medx.beta.dto.PagoRequest;
import com.medx.beta.dto.PagoResponse;
import com.medx.beta.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<PagoResponse>> listar() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PagoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findById(id));
    }

    @GetMapping("/cita/{citaId}")
    @PreAuthorize("hasAnyRole('DOCTOR','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<PagoResponse>> porCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(pagoService.findByCita(citaId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PagoResponse> crear(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PagoResponse> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody PagoRequest request) {
        return ResponseEntity.ok(pagoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

