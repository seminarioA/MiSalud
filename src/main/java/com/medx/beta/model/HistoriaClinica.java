package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historias_clinicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_clinica")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_historias_clinicas__pacientes"))
    private Paciente paciente;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;
}

