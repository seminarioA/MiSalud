package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Person_Photo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_photo__person"))
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tipo tipo = Tipo.PROFILE;

    @Column(nullable = false, length = 500)
    private String archivoUrl;

    private String archivoTipo;
    private Integer tamanoBytes;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Tipo {
        PROFILE, DOCUMENTO, OTRO
    }
}
