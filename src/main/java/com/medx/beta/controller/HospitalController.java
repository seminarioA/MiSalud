package com.medx.beta.controller;

import com.medx.beta.dto.HospitalRequest;
import com.medx.beta.dto.HospitalResponse;
import com.medx.beta.service.HospitalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitales")
@Validated
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    @PreAuthorize("permitAll()") // Público - cualquiera puede ver hospitales
    public ResponseEntity<List<HospitalResponse>> getAllHospitales() {
        return ResponseEntity.ok(hospitalService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()") // Público - cualquiera puede ver un hospital específico
    public ResponseEntity<HospitalResponse> getHospitalById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(hospitalService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HospitalResponse> createHospital(@RequestBody @Valid HospitalRequest hospitalRequest) {
        HospitalResponse created = hospitalService.create(hospitalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HospitalResponse> updateHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                                   @RequestBody @Valid HospitalRequest hospitalRequest) {
        return ResponseEntity.ok(hospitalService.update(id, hospitalRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar hospitales
    public ResponseEntity<Void> deleteHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        hospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
