package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Paciente")

@Getter
@Setter
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pacienteId;

    @Column(nullable = false, length = 75)
    private String primerNombre;

    @Column(length = 75)
    private String segundoNombre;

    @Column(nullable = false, length = 75)
    private String primerApellido;

    @Column(nullable = false, length = 75)
    private String segundoApellido;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(length = 500)
    private String domicilio;

    @Column(nullable = false)
    private Boolean estaActivo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<CitaMedica> citas;

    // Getters y Setters
}
