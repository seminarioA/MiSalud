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

@RestController
@RequestMapping("/api/hospitales")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public List<Hospital> getAllHospitales() { // nombre del método público puede mantenerse por compatibilidad externa
        return hospitalService.getAll();
    }

    @GetMapping("/{id}")
    public Hospital getHospitalById(@PathVariable Integer id) {
        return hospitalService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) {
        Hospital created = hospitalService.create(hospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Hospital updateHospital(@PathVariable Integer id, @RequestBody Hospital hospital) {
        return hospitalService.update(id, hospital);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Integer id) {
        hospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}