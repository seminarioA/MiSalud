package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Sys_Role",
        uniqueConstraints = @UniqueConstraint(name = "uq_sys_role__nombre", columnNames = "nombre"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SysRole {

    @Id
    @Column(length = 40)
    private String code;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;
}
