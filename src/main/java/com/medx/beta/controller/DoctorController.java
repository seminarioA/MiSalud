package com.medx.beta.controller;

import com.medx.beta.dto.DoctorRequest;
import com.medx.beta.dto.DoctorResponse;
import com.medx.beta.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctores")
@RequiredArgsConstructor
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA')")
    public ResponseEntity<List<DoctorResponse>> listar() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPCIONISTA')")
    public ResponseEntity<DoctorResponse> obtener(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> crear(@RequestBody @Valid DoctorRequest doctorRequest) {
        DoctorResponse creado = doctorService.create(doctorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and #id == authentication.principal.doctorId)")
    public ResponseEntity<DoctorResponse> actualizar(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                             @RequestBody @Valid DoctorRequest doctorRequest) {
        return ResponseEntity.ok(doctorService.update(id, doctorRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
