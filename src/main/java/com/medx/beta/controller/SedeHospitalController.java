package com.medx.beta.controller;

import com.medx.beta.model.SedeHospital;
import com.medx.beta.service.SedeHospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
@Validated
public class SedeHospitalController {

    private final SedeHospitalService sedeHospitalService;

    @GetMapping
    public ResponseEntity<List<SedeHospital>> getAll() {
        return ResponseEntity.ok(sedeHospitalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeHospital> getById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(sedeHospitalService.getById(id));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<SedeHospital>> getByHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer hospitalId) {
        return ResponseEntity.ok(sedeHospitalService.getByHospital(hospitalId));
    }

    @PostMapping
    public ResponseEntity<SedeHospital> create(@RequestBody @Valid SedeHospital sedeHospital) {
        SedeHospital creada = sedeHospitalService.create(sedeHospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeHospital> update(@PathVariable @Positive(message = "El id debe ser positivo") Integer id, @RequestBody @Valid SedeHospital sedeHospital) {
        return ResponseEntity.ok(sedeHospitalService.update(id, sedeHospital));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        sedeHospitalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
