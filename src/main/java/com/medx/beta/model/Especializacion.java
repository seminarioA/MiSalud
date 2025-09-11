package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Especializacion")

@Getter
@Setter
public class Especializacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer especializacionId;

    @NotBlank(message = "El nombre de la especialización es obligatorio")
    @Size(max = 70, message = "El nombre no debe exceder los 70 caracteres")
    @Column(nullable = false, length = 70)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    @ManyToMany(mappedBy = "especializaciones", fetch = FetchType.LAZY)
    private List<Doctor> doctores;
}
