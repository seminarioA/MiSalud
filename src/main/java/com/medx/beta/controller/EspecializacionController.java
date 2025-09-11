package com.medx.beta.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.medx.beta.service.EspecializacionService;
import com.medx.beta.model.Especializacion;
import jakarta.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen
@RequestMapping("/api/especializaciones")
public class EspecializacionController {
    
    @Autowired
    private EspecializacionService especializacionService;
    
    @GetMapping
    public ResponseEntity<List<Especializacion>> getAllEspecializaciones() {
        List<Especializacion> especializaciones = especializacionService.getAll();
        return ResponseEntity.ok(especializaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especializacion> getEspecializacionById(@PathVariable Integer id) {
        Especializacion especializacion = especializacionService.getById(id);
        return ResponseEntity.ok(especializacion);
    }

    @PostMapping
    public ResponseEntity<?> createEspecializacion(@Valid @RequestBody Especializacion especializacion) {
        try {
            // Con la anotación @Valid, Spring ya validará el objeto
            // y lanzará una excepción si hay errores de validación
            
            // Validar si ya existe una especialización con el mismo nombre
            if (especializacionService.existsByNombre(especializacion.getNombre())) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Error al crear la especialización");
                response.put("error", "Ya existe una especialización con el nombre: " + especializacion.getNombre());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            Especializacion nuevaEspecializacion = especializacionService.create(especializacion);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Especialización creada con éxito");
            response.put("especializacion", nuevaEspecializacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al crear la especialización");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEspecializacion(
            @PathVariable Integer id, 
            @Valid @RequestBody Especializacion especializacion) {
        try {
            // Validar si existe otra especialización con el mismo nombre
            if (especializacionService.existsByNombreAndNotId(especializacion.getNombre(), id)) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Error al actualizar la especialización");
                response.put("error", "Ya existe otra especialización con el nombre: " + especializacion.getNombre());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            Especializacion actualizadaEspecializacion = especializacionService.update(id, especializacion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Especialización actualizada con éxito");
            response.put("especializacion", actualizadaEspecializacion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Error al actualizar la especialización");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEspecializacion(@PathVariable Integer id) {
        especializacionService.deleteEspecializacion(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}