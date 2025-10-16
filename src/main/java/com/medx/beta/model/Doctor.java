package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Doctor")

@Getter
@Setter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;

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

    @NotNull(message = "La sede del hospital es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sede_id", nullable = false, foreignKey = @ForeignKey(name = "fk_doctor_sede"))
    private SedeHospital sedeHospital;

    @NotNull(message = "El turno es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false, foreignKey = @ForeignKey(name = "fk_doctor_turno"))
    private HorarioBase turno;

    @Column(name = "esta_activo", nullable = false)
    private Boolean estaActivo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "Doctor_Especializacion",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "especializacion_id")
    )
    private List<Especializacion> especializaciones;

    @PrePersist
    private void prePersist() {
        normalize();
        if (estaActivo == null) {
            estaActivo = true;
        }
        LocalDateTime now = LocalDateTime.now();
        fechaCreacion = now;
        fechaActualizacion = now;
    }

    @PreUpdate
    private void preUpdate() {
        normalize();
        fechaActualizacion = LocalDateTime.now();
    }

    private void normalize() {
        primerNombre = trim(primerNombre);
        segundoNombre = trim(segundoNombre);
        primerApellido = trim(primerApellido);
        segundoApellido = trim(segundoApellido);
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }
}
