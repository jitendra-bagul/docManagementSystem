package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role_m")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;
    private String description;
}
