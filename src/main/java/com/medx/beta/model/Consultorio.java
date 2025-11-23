package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultorios", uniqueConstraints = {
        @UniqueConstraint(name = "uq_consultorios__nombre", columnNames = "nombre_o_numero")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consultorio")
    private Long id;

    @Column(name = "nombre_o_numero", nullable = false, length = 20)
    private String nombreONumero;
}

