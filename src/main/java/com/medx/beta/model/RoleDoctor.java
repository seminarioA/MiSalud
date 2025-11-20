package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Role_Doctor", uniqueConstraints = {
        @UniqueConstraint(name = "uq_role_doctor__licencia", columnNames = "licencia_profesional")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDoctor {

    @Id
    private Long personRoleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "person_role_id", foreignKey = @ForeignKey(name = "fk_role_doctor__pr"))
    private PersonRole personRole;

    @Column(nullable = false, length = 50)
    private String licenciaProfesional;

    private LocalDate fechaLicencia;
}