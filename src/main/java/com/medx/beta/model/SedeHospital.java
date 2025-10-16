package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Sede_Hospital", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"hospital_id", "sede"}, name = "uk_sede_nombre_hospital")
})

@Getter
@Setter
public class SedeHospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sedeId;

    @NotBlank(message = "El nombre de la sede es obligatorio")
    @Size(max = 100, message = "El nombre de la sede no debe exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String sede;

    @Size(max = 255, message = "La ubicación no debe exceder 255 caracteres")
    @Column(length = 255)
    private String ubicacion;

    @NotNull(message = "El hospital es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sede_hospital_hospital"))
    private Hospital hospital;

    @OneToMany(mappedBy = "sedeHospital", fetch = FetchType.LAZY)
    private List<Doctor> doctores;

    @Column(name = "fecha_creacion", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private java.time.LocalDateTime fechaCreacion;
}
