package com.medx.beta.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "User_Sys_Role")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(UserSysRoleId.class)
public class UserSysRole {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_usr_sys_role__user"))
    private AuthUser user;

    @Id
    @ManyToOne
    @JoinColumn(name = "sys_role", foreignKey = @ForeignKey(name = "fk_usr_sys_role__role"))
    private SysRole sysRole;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
