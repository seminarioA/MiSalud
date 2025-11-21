package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "Person",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_person__doc", columnNames = "documento_identidad"),
                @UniqueConstraint(name = "uq_person__email", columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 75)
    private String primerNombre;

    private String segundoNombre;

    @Column(nullable = false, length = 75)
    private String primerApellido;

    private String segundoApellido;

    @Column(nullable = false, length = 20)
    private String documentoIdentidad;

    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private Boolean estaActivo = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PersonRole> roles = List.of();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PersonPhoto> fotos = List.of();
}