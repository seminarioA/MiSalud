package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "responsables_pacientes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_responsable_paciente", columnNames = {"id_paciente", "id_persona_responsable"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsablePaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_responsable_paciente")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false,
            foreignKey = @ForeignKey(name = "fk_responsables_pacientes__pacientes"))
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_persona_responsable", nullable = false,
            foreignKey = @ForeignKey(name = "fk_responsables_pacientes__personas"))
    private Persona personaResponsable;

    @Column(name = "relacion", nullable = false, length = 50)
    private String relacion;
}

