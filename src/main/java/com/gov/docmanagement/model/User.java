package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "user_m")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;

    private String password;

    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
