package com.medx.beta.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Hospital")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hospitalId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY)
    private List<SedeHospital> sedes;

    // Getters y Setters
}
