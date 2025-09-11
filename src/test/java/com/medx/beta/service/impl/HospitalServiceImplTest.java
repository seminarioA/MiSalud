package com.medx.beta.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medx.beta.model.Hospital;
import com.medx.beta.repository.HospitalRepository;

@ExtendWith(MockitoExtension.class)
public class HospitalServiceImplTest {

    @Mock
    private HospitalRepository hospitalRepository;
    
    @InjectMocks
    private HospitalServiceImpl hospitalService;
    
    private Hospital hospital;
    
    @BeforeEach
    void setUp() {
        // Configurar un objeto Hospital para usar en las pruebas
        hospital = new Hospital();
        hospital.setHospitalId(1);
        hospital.setNombre("Hospital de Prueba");
        hospital.setDescripcion("Descripción de prueba");
    }
    
    @Test
    @DisplayName("Test para obtener todos los hospitales")
    void testGetAllHospitales() {
        // Arrange
        List<Hospital> hospitales = Arrays.asList(hospital, new Hospital());
        when(hospitalRepository.findAll()).thenReturn(hospitales);
        
        // Act
        List<Hospital> resultado = hospitalService.getAll();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(hospitalRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("Test para obtener hospital por ID existente")
    void testGetHospitalByIdExistente() {
        // Arrange
        when(hospitalRepository.findById(1)).thenReturn(Optional.of(hospital));
        
        // Act
        Hospital resultado = hospitalService.getById(1);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Hospital de Prueba", resultado.getNombre());
        verify(hospitalRepository, times(1)).findById(1);
    }
    
    @Test
    @DisplayName("Test para obtener hospital por ID no existente")
    void testGetHospitalByIdNoExistente() {
        // Arrange
        when(hospitalRepository.findById(99)).thenReturn(Optional.empty());
        
        // Act
        Hospital resultado = hospitalService.getById(99);
        
        // Assert
        assertNull(resultado);
        verify(hospitalRepository, times(1)).findById(99);
    }
    
    @Test
    @DisplayName("Test para crear un hospital")
    void testCreateHospital() {
        // Arrange
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);
        
        // Act
        Hospital resultado = hospitalService.create(hospital);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Hospital de Prueba", resultado.getNombre());
        verify(hospitalRepository, times(1)).save(hospital);
    }
    
    @Test
    @DisplayName("Test para actualizar un hospital existente")
    void testUpdateHospitalExistente() {
        // Arrange
        Hospital hospitalActualizado = new Hospital();
        hospitalActualizado.setNombre("Hospital Actualizado");
        hospitalActualizado.setDescripcion("Descripción actualizada");
        
        when(hospitalRepository.findById(1)).thenReturn(Optional.of(hospital));
        when(hospitalRepository.save(any(Hospital.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Hospital resultado = hospitalService.update(1, hospitalActualizado);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Hospital Actualizado", resultado.getNombre());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        verify(hospitalRepository, times(1)).findById(1);
        verify(hospitalRepository, times(1)).save(any(Hospital.class));
    }
    
    @Test
    @DisplayName("Test para actualizar un hospital no existente")
    void testUpdateHospitalNoExistente() {
        // Arrange
        when(hospitalRepository.findById(99)).thenReturn(Optional.empty());
        
        // Act
        Hospital resultado = hospitalService.update(99, hospital);
        
        // Assert
        assertNull(resultado);
        verify(hospitalRepository, times(1)).findById(99);
        verify(hospitalRepository, never()).save(any(Hospital.class));
    }
    
    @Test
    @DisplayName("Test para eliminar un hospital")
    void testDeleteHospital() {
        // Arrange
        doNothing().when(hospitalRepository).deleteById(1);
        
        // Act
        hospitalService.deleteHospital(1);
        
        // Assert
        verify(hospitalRepository, times(1)).deleteById(1);
    }
    
    @Test
    @DisplayName("Test para buscar hospitales por nombre")
    void testFindHospitalesByNombre() {
        // Arrange
        List<Hospital> hospitales = Arrays.asList(hospital);
        when(hospitalRepository.findByNombreContainingIgnoreCase("Prueba")).thenReturn(hospitales);
        
        // Act
        List<Hospital> resultado = hospitalService.findHospitalesByNombre("Prueba");
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hospital de Prueba", resultado.get(0).getNombre());
        verify(hospitalRepository, times(1)).findByNombreContainingIgnoreCase("Prueba");
    }
    
    @Test
    @DisplayName("Test para validar la entrada al crear un hospital")
    void testValidacionCrearHospital() {
        // Arrange
        Hospital hospitalInvalido = new Hospital();
        // No asignamos nombre para simular una entrada inválida
        
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalInvalido);
        
        // Act
        Hospital resultado = hospitalService.create(hospitalInvalido);
        
        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getNombre());
        verify(hospitalRepository, times(1)).save(hospitalInvalido);
    }
    
    @Test
    @DisplayName("Test para validar la entrada al actualizar un hospital")
    void testValidacionActualizarHospital() {
        // Arrange
        Hospital hospitalInvalido = new Hospital();
        // No asignamos nombre para simular una entrada inválida
        
        when(hospitalRepository.findById(1)).thenReturn(Optional.of(hospital));
        when(hospitalRepository.save(any(Hospital.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        Hospital resultado = hospitalService.update(1, hospitalInvalido);
        
        // Assert
        assertNotNull(resultado);
        // La validación en el servicio permite actualizar con un nombre nulo
        assertNull(resultado.getNombre());
        verify(hospitalRepository, times(1)).findById(1);
        verify(hospitalRepository, times(1)).save(any(Hospital.class));
    }
}
