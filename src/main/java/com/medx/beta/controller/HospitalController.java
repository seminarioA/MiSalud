package com.medx.beta.controller;

import com.medx.beta.exception.NotFoundException;
import com.medx.beta.model.Hospital;
import com.medx.beta.service.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen
@RequestMapping("/api/hospitales")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<?> getAllHospitales() {
        List<Hospital> hospitales = hospitalService.getAll();
        if (hospitales.isEmpty()) {
            return buildMessage(HttpStatus.NOT_FOUND, "No se encontraron hospitales en la base de datos");
        }
        return ResponseEntity.ok(hospitales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHospitalById(@PathVariable Integer id) {
        try {
            Hospital hospital = hospitalService.getById(id);
            if (hospital == null) {
                return buildMessage(HttpStatus.NOT_FOUND,
                        "El hospital con ID: " + id + " no existe en la base de datos");
            }
            return ResponseEntity.ok(hospital);
        } catch (NotFoundException ex) {
            return buildMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createHospital(@Valid @RequestBody Hospital hospital, BindingResult result) {
        if (result.hasErrors()) {
            return buildValidationErrors(result);
        }

        if (hospitalService.existsByNombre(hospital.getNombre())) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al crear el hospital");
            response.put("error", "Ya existe un hospital con el nombre: " + hospital.getNombre());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Hospital nuevoHospital = hospitalService.create(hospital);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Hospital creado con éxito");
        response.put("hospital", nuevoHospital);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHospital(@PathVariable Integer id,
                                            @Valid @RequestBody Hospital hospital,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return buildValidationErrors(result);
        }

        try {
            Hospital hospitalExistente = hospitalService.getById(id);
            if (hospitalExistente == null) {
                return buildMessage(HttpStatus.NOT_FOUND,
                        "El hospital con ID: " + id + " no existe en la base de datos");
            }
        } catch (NotFoundException ex) {
            return buildMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        if (hospitalService.existsByNombreAndNotId(hospital.getNombre(), id)) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar el hospital");
            response.put("error", "Ya existe otro hospital con el nombre: " + hospital.getNombre());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Hospital hospitalActualizado = hospitalService.update(id, hospital);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Hospital actualizado con éxito");
        response.put("hospital", hospitalActualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHospital(@PathVariable Integer id) {
        try {
            Hospital hospital = hospitalService.getById(id);
            if (hospital == null) {
                return buildMessage(HttpStatus.NOT_FOUND,
                        "El hospital con ID: " + id + " no existe en la base de datos");
            }
            hospitalService.deleteHospital(id);
            return buildMessage(HttpStatus.OK, "Hospital eliminado con éxito");
        } catch (NotFoundException ex) {
            return buildMessage(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchHospitalesByNombre(@RequestParam String nombre) {
        List<Hospital> hospitales = hospitalService.findHospitalesByNombre(nombre);
        if (hospitales.isEmpty()) {
            return buildMessage(HttpStatus.NOT_FOUND,
                    "No se encontraron hospitales con el nombre: " + nombre);
        }
        return ResponseEntity.ok(hospitales);
    }

    private ResponseEntity<Map<String, Object>> buildValidationErrors(BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errores = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }
        response.put("mensaje", "Campos inválidos");
        response.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ResponseEntity<Map<String, Object>> buildMessage(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", message);
        return ResponseEntity.status(status).body(response);
    }
}
