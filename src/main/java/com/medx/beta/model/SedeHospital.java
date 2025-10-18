package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Sede_Hospital",
        uniqueConstraints = @UniqueConstraint(name = "uq_sede__hospital_nombre", columnNames = {"hospital_id", "nombre"}))
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class SedeHospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hospital_id", foreignKey = @ForeignKey(name = "fk_sede__hospital"))
    private Hospital hospital;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String ubicacion;
    private String telefono;
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}