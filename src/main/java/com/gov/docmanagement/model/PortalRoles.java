package com.gov.docmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "portal_role_m")
@Data
public class PortalRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String portalRole;

    @NotNull
    private String description;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role roleId;
}