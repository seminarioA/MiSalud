package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Hospital", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nombre", name = "uk_hospital_nombre")
})

@Getter
@Setter
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hospitalId;

    @NotBlank(message = "El nombre del hospital es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY)
    private List<SedeHospital> sedes;
}
