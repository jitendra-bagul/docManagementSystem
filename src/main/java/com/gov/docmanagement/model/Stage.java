package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stage_m")
@Data
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stagename; // Uploaded, WaitingForClerkApproval, ...
}
