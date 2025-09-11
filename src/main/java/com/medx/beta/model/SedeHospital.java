package com.medx.beta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Sede_Hospital")

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Hospital_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_Sede_Hospital_Hospital1"))
    private Hospital hospital;

    @OneToMany(mappedBy = "sedeHospital", fetch = FetchType.LAZY)
    private List<Doctor> doctores;
}
