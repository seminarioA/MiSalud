package com.medx.beta.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Especializacion")
public class Especializacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer especializacionId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @ManyToMany(mappedBy = "especializaciones", fetch = FetchType.LAZY)
    private List<Doctor> doctores;

    // Getters y Setters
}
