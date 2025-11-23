package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_doctores__personas"))
    private Persona persona;

    @Column(name = "numero_colegiatura", length = 20)
    private String numeroColegiatura;
}

