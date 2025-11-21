package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Person_Role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_person_role__person"))
    private Person person;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_code", foreignKey = @ForeignKey(name = "fk_person_role__role"))
    private SysRole sysRole;

    @ManyToOne
    @JoinColumn(name = "sede_id", foreignKey = @ForeignKey(name = "fk_person_role__sede"))
    private SedeHospital sede;

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        ACTIVE, INACTIVE, SUSPENDED
    }
}