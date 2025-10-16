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

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitales")
@Validated
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<List<HospitalResponse>> getAllHospitales() {
        return ResponseEntity.ok(hospitalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> getHospitalById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(hospitalService.getById(id));
    }

    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(@RequestBody @Valid HospitalRequest hospitalRequest) {
        HospitalResponse created = hospitalService.create(hospitalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> updateHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                                   @RequestBody @Valid HospitalRequest hospitalRequest) {
        return ResponseEntity.ok(hospitalService.update(id, hospitalRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        hospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
