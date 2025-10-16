package com.medx.beta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medx.beta.service.HospitalService;
import com.medx.beta.dto.HospitalRequest;
import com.medx.beta.dto.HospitalResponse;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/hospitales")
@Validated
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public List<HospitalResponse> getAllHospitales() {
        return hospitalService.getAll();
    }

    @GetMapping("/{id}")
    public HospitalResponse getHospitalById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return hospitalService.getById(id);
    }

    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(@RequestBody @Valid HospitalRequest hospitalRequest) {
        HospitalResponse created = hospitalService.create(hospitalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public HospitalResponse updateHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                   @RequestBody @Valid HospitalRequest hospitalRequest) {
        return hospitalService.update(id, hospitalRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        hospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}