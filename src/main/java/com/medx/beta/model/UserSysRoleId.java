package com.medx.beta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSysRoleId implements Serializable {
    private Long user;
    private String sysRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSysRoleId)) return false;
        UserSysRoleId that = (UserSysRoleId) o;
        return Objects.equals(user, that.user) && Objects.equals(sysRole, that.sysRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, sysRole);
    }
}
