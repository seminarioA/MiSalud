package com.medx.beta.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class HospitalValidationTest {

    private static Validator validator;
    
    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    @DisplayName("Test validación nombre no vacío")
    void testNombreNoVacio() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setNombre(""); // Nombre vacío
        hospital.setDescripcion("Descripción de prueba");
        
        // Act
        Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
        
        // Assert
        assertEquals(1, violations.size());
        assertEquals("El nombre del hospital es obligatorio", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Test validación nombre muy corto")
    void testNombreMuyCorto() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setNombre("AB"); // Menos de 3 caracteres
        hospital.setDescripcion("Descripción de prueba");
        
        // Act
        Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
        
        // Assert
        assertEquals(1, violations.size());
        assertEquals("El nombre debe tener entre 3 y 100 caracteres", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Test validación nombre muy largo")
    void testNombreMuyLargo() {
        // Arrange
        Hospital hospital = new Hospital();
        // Nombre con más de 100 caracteres
        hospital.setNombre("A".repeat(101));
        hospital.setDescripcion("Descripción de prueba");
        
        // Act
        Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
        
        // Assert
        assertEquals(1, violations.size());
        assertEquals("El nombre debe tener entre 3 y 100 caracteres", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Test validación descripción muy larga")
    void testDescripcionMuyLarga() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setNombre("Hospital de Prueba");
        // Descripción con más de 500 caracteres
        hospital.setDescripcion("A".repeat(501));
        
        // Act
        Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
        
        // Assert
        assertEquals(1, violations.size());
        assertEquals("La descripción no puede exceder los 500 caracteres", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Test hospital válido")
    void testHospitalValido() {
        // Arrange
        Hospital hospital = new Hospital();
        hospital.setNombre("Hospital de Prueba");
        hospital.setDescripcion("Descripción de prueba");
        
        // Act
        Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
        
        // Assert
        assertTrue(violations.isEmpty());
    }
}
