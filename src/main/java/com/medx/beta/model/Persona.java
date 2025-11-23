package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "personas", uniqueConstraints = {
        @UniqueConstraint(name = "uq_personas__numero_documento", columnNames = "numero_documento")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @Column(name = "primer_nombre", nullable = false, length = 50)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @Column(name = "primer_apellido", nullable = false, length = 50)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 15)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false, length = 15)
    private Genero genero;

    @Column(name = "numero_telefono", length = 9)
    private String numeroTelefono;

    @Column(name = "url_foto_perfil", length = 255)
    private String urlFotoPerfil;

    public enum TipoDocumento {
        DNI, CE, PASAPORTE, PTP, RUC
    }

    public enum Genero {
        MASCULINO, FEMENINO, OTRO
    }
}

