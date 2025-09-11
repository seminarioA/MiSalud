package com.medx.beta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medx.beta.model.Hospital;
import com.medx.beta.service.HospitalService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import jakarta.validation.Valid;


@RestController
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen
@RequestMapping("/api/hospitales")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<?> getAllHospitales() {
        try {
            List<Hospital> hospitales = hospitalService.getAll();
            if (hospitales.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron hospitales en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(hospitales, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al consultar los hospitales");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHospitalById(@PathVariable Integer id) {
        try {
            Hospital hospital = hospitalService.getById(id);
            if (hospital == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "El hospital con ID: " + id + " no existe en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(hospital, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al consultar el hospital con ID: " + id);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createHospital(@Valid @RequestBody Hospital hospital, BindingResult result) {
        try {
            // Verificamos si hay errores de validacion
            if (result.hasErrors()) {
                Map<String, Object> response = new HashMap<>();
                Map<String, String> errores = new HashMap<>();
                
                for (FieldError error : result.getFieldErrors()) {
                    errores.put(error.getField(), error.getDefaultMessage());
                }
                
                response.put("mensaje", "Campos inválidos");
                response.put("errores", errores);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Validar si ya existe un hospital con el mismo nombre
            if (hospitalService.existsByNombre(hospital.getNombre())) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Error al crear el hospital");
                response.put("error", "Ya existe un hospital con el nombre: " + hospital.getNombre());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            Hospital nuevoHospital = hospitalService.create(hospital);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Hospital creado con éxito");
            response.put("hospital", nuevoHospital);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al crear el hospital");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHospital(@PathVariable Integer id, @Valid @RequestBody Hospital hospital, BindingResult result) {
        try {
            // Verificamos si hay errores de validacion
            if (result.hasErrors()) {
                Map<String, Object> response = new HashMap<>();
                Map<String, String> errores = new HashMap<>();
                
                for (FieldError error : result.getFieldErrors()) {
                    errores.put(error.getField(), error.getDefaultMessage());
                }
                
                response.put("mensaje", "Campos inválidos");
                response.put("errores", errores);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Validar si existe el hospital que se quiere actualizar
            Hospital hospitalExistente = hospitalService.getById(id);
            if (hospitalExistente == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "El hospital con ID: " + id + " no existe en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            // Validar si ya existe otro hospital con el mismo nombre
            if (hospitalService.existsByNombreAndNotId(hospital.getNombre(), id)) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Error al actualizar el hospital");
                response.put("error", "Ya existe otro hospital con el nombre: " + hospital.getNombre());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            Hospital hospitalActualizado = hospitalService.update(id, hospital);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Hospital actualizado con éxito");
            response.put("hospital", hospitalActualizado);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar el hospital");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHospital(@PathVariable Integer id) {
        try {
            Hospital hospital = hospitalService.getById(id);
            if (hospital == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "El hospital con ID: " + id + " no existe en la base de datos");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            hospitalService.deleteHospital(id);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Hospital eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al eliminar el hospital");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchHospitalesByNombre(@RequestParam String nombre) {
        try {
            List<Hospital> hospitales = hospitalService.findHospitalesByNombre(nombre);
            if (hospitales.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "No se encontraron hospitales con el nombre: " + nombre);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(hospitales, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al buscar hospitales por nombre");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}