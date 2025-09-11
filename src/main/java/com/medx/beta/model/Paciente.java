package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 75, message = "El primer nombre no debe exceder 75 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El primer nombre solo puede contener letras y espacios")
    @Column(nullable = false, length = 75)
    private String primerNombre;

    @Size(max = 75, message = "El segundo nombre no debe exceder 75 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El segundo nombre solo puede contener letras y espacios")
    @Column(length = 75)
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 75, message = "El primer apellido no debe exceder 75 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El primer apellido solo puede contener letras y espacios")
    @Column(nullable = false, length = 75)
    private String primerApellido;

    @NotBlank(message = "El segundo apellido es obligatorio")
    @Size(max = 75, message = "El segundo apellido no debe exceder 75 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El segundo apellido solo puede contener letras y espacios")
    @Column(nullable = false, length = 75)
    private String segundoApellido;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Size(max = 500, message = "El domicilio no debe exceder 500 caracteres")
    @Column(length = 500)
    private String domicilio;

    @NotNull(message = "El estado de actividad es obligatorio")
    @Column(nullable = false)
    private Boolean estaActivo = true;

    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<CitaMedica> citas;

    @PrePersist
    @PreUpdate
    private void normalize() {
        primerNombre = trim(primerNombre);
        segundoNombre = trim(segundoNombre);
        primerApellido = trim(primerApellido);
        segundoApellido = trim(segundoApellido);
        domicilio = trim(domicilio);
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }
}
