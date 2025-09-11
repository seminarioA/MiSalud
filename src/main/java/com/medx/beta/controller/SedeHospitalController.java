package com.medx.beta.controller;

import com.medx.beta.model.SedeHospital;
import com.medx.beta.service.SedeHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
public class SedeHospitalController {

    private final SedeHospitalService sedeHospitalService;

    @GetMapping
    public ResponseEntity<List<SedeHospital>> getAll() {
        return ResponseEntity.ok(sedeHospitalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeHospital> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(sedeHospitalService.getById(id));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<SedeHospital>> getByHospital(@PathVariable Integer hospitalId) {
        return ResponseEntity.ok(sedeHospitalService.getByHospital(hospitalId));
    }

    @PostMapping
    public ResponseEntity<SedeHospital> create(@RequestBody SedeHospital sedeHospital) {
        SedeHospital creada = sedeHospitalService.create(sedeHospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeHospital> update(@PathVariable Integer id, @RequestBody SedeHospital sedeHospital) {
        return ResponseEntity.ok(sedeHospitalService.update(id, sedeHospital));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sedeHospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

