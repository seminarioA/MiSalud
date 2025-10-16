package com.medx.beta.controller;

import com.medx.beta.dto.EspecializacionRequest;
import com.medx.beta.dto.EspecializacionResponse;
import com.medx.beta.service.EspecializacionService;
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
@RequestMapping("/api/v1/especializaciones")
@Validated
@RequiredArgsConstructor
public class EspecializacionController {

    private final EspecializacionService especializacionService;

    @GetMapping
    public ResponseEntity<List<EspecializacionResponse>> getAllEspecializaciones() {
        return ResponseEntity.ok(especializacionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecializacionResponse> getEspecializacionById(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        return ResponseEntity.ok(especializacionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EspecializacionResponse> createEspecializacion(@RequestBody @Valid EspecializacionRequest especializacionRequest) {
        EspecializacionResponse creada = especializacionService.create(especializacionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecializacionResponse> updateEspecializacion(@PathVariable @Positive(message = "El id debe ser positivo") Integer id,
                                                                         @RequestBody @Valid EspecializacionRequest especializacionRequest) {
        return ResponseEntity.ok(especializacionService.update(id, especializacionRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEspecializacion(@PathVariable @Positive(message = "El id debe ser positivo") Integer id) {
        especializacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
