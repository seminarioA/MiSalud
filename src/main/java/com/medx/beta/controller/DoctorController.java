package com.medx.beta.controller;

import com.medx.beta.model.Doctor;
import com.medx.beta.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<Doctor>> listar() {
        return ResponseEntity.ok(doctorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Doctor> crear(@RequestBody Doctor doctor) {
        Doctor creado = doctorService.create(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> actualizar(@PathVariable Integer id, @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.update(id, doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

