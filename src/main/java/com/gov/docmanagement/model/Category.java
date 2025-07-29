package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "category_m")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
}
