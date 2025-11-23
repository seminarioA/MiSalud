package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "registro_citas_medicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroCitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_historia_clinica", nullable = false,
            foreignKey = @ForeignKey(name = "fk_registro_citas_medicas__historias_clinicas"))
    private HistoriaClinica historiaClinica;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cita", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_registro_citas_medicas__citas"))
    private Cita cita;

    @Lob
    @Column(name = "notas_medicas")
    private String notasMedicas;
}

