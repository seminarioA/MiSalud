package com.medx.beta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medx.beta.model.Hospital;
import com.medx.beta.service.HospitalService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/hospitales")
@Validated
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public List<Hospital> getAllHospitales() { // nombre del método público puede mantenerse por compatibilidad externa
        return hospitalService.getAll();
    }

    @GetMapping("/{id}")
    public Hospital getHospitalById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return hospitalService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Hospital> createHospital(@RequestBody @Valid Hospital hospital) {
        Hospital created = hospitalService.create(hospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Hospital updateHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                   @RequestBody @Valid Hospital hospital) {
        return hospitalService.update(id, hospital);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        hospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}