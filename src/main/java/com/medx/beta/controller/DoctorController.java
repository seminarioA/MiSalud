package com.medx.beta.controller;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import com.medx.beta.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctores")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<DoctorResponse>> listar() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','PACIENTE','RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<DoctorResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<DoctorResponse> crear(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<DoctorResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
