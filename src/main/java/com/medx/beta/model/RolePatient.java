package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Role_Patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePatient {

    @Id
    private Long personRoleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "person_role_id", foreignKey = @ForeignKey(name = "fk_role_patient__pr"))
    private PersonRole personRole;

    private String numeroHistoria;
    private String aseguradora;
    private String planSeguro;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CitaMedica> citas;
}