package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "tag_m")
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagColor;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
}