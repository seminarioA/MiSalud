package com.medx.beta.controller;

import com.medx.beta.dto.PersonaRequest;
import com.medx.beta.dto.PersonaResponse;
import com.medx.beta.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonService personService;

    @GetMapping
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<List<PersonaResponse>> listar() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','OPERACIONES')")
    public ResponseEntity<PersonaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<PersonaResponse> crear(@Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.ok(personService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<PersonaResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody PersonaRequest request) {
        return ResponseEntity.ok(personService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERACIONES')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
