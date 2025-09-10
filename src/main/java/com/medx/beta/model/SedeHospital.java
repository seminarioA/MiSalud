package com.medx.beta.model;

import jakarta.persistence.*;
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

    @Column(nullable = false, length = 100)
    private String sede;

    @Column(length = 255)
    private String ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Hospital_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_Sede_Hospital_Hospital1"))
    private Hospital hospital;

    @OneToMany(mappedBy = "sedeHospital", fetch = FetchType.LAZY)
    private List<Doctor> doctores;
}
