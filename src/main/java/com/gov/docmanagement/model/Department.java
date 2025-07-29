package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "department_m")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
