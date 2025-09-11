package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Doctor")

@Getter
@Setter
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorId;

    @Column(nullable = false, length = 75)
    private String primerNombre;

    @Column(length = 75)
    private String segundoNombre;

    @Column(nullable = false, length = 75)
    private String primerApellido;

    @Column(nullable = false, length = 75)
    private String segundoApellido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Sede_Hospital_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_Doctor_Sede_Hospital1"))
    private SedeHospital sedeHospital;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<CitaMedica> citas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Doctor_Especializacion",
            joinColumns = @JoinColumn(name = "Doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "Especializacion_id")
    )
    private List<Especializacion> especializaciones;
}
