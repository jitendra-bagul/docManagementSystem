package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "portal_role_m")
@Data
public class PortalRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String portalRole;
    private String description;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role roleId;
}