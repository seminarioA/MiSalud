package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Auth_User", uniqueConstraints = {
        @UniqueConstraint(name = "uq_auth_user__username", columnNames = "username")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_auth_user__person"))
    private Person person;

    @OneToOne
    @JoinColumn(name = "profile_photo_id", foreignKey = @ForeignKey(name = "fk_auth_user__photo"))
    private PersonPhoto profilePhoto;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Status status = Status.ACTIVE;

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        ACTIVE, INACTIVE, LOCKED
    }
}
