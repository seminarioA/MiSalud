package com.medx.beta.controller;

import com.medx.beta.dto.SedeHospitalRequest;
import com.medx.beta.dto.SedeHospitalResponse;
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
@RequestMapping("/api/v1/sedes")
@RequiredArgsConstructor
@Validated
public class SedeHospitalController {

    private final SedeHospitalService sedeHospitalService;

    @GetMapping
    public ResponseEntity<List<SedeHospitalResponse>> getAll() {
        return ResponseEntity.ok(sedeHospitalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeHospitalResponse> getById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(sedeHospitalService.getById(id));
    }

    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<SedeHospitalResponse>> getByHospital(@PathVariable @Positive(message = "El id debe ser positivo") Integer hospitalId) {
        return ResponseEntity.ok(sedeHospitalService.getByHospital(hospitalId));
    }

    @PostMapping
    public ResponseEntity<SedeHospitalResponse> create(@RequestBody @Valid SedeHospitalRequest sedeHospitalRequest) {
        SedeHospitalResponse creada = sedeHospitalService.create(sedeHospitalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeHospitalResponse> update(@PathVariable @Positive(message = "El id debe ser positivo") Integer id, @RequestBody @Valid SedeHospitalRequest sedeHospitalRequest) {
        return ResponseEntity.ok(sedeHospitalService.update(id, sedeHospitalRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        sedeHospitalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
