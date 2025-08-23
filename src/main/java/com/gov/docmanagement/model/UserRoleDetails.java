package com.gov.docmanagement.model;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserRoleDetails {
    User user;
    PortalRoles portalRoles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PortalRoles getPortalRoles() {
        return portalRoles;
    }

    public void setPortalRoles(PortalRoles portalRoles) {
        this.portalRoles = portalRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleDetails that = (UserRoleDetails) o;
        return Objects.equals(user, that.user) && Objects.equals(portalRoles, that.portalRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, portalRoles);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRoleDetails {");
        sb.append("user=").append(user);
        sb.append(", portalRoles=").append(portalRoles);
        sb.append('}');
        return sb.toString();
    }
}
