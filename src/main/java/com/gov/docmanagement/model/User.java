package com.gov.docmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "user_m")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "First Name is Required")
    @Size(min = 3, max = 20, message = "First Name should be between 3 and 20 characters")
    private String firstName;

    @NotNull(message = "Last Name is Required")
    @Size(min = 3, max = 20, message = "Last Name should be between 3 and 20 characters")
    private String lastName;
    @Column(unique = true)
    private String username;

    @NotNull(message = "Password is Required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Email
    @NotBlank
    @NotNull(message = "Email Id is Required")
    private String emailId;

    private Date updatedDate;

    @NotNull(message = "Phone number is required")
    @Min(value = 1000000000L, message = "Phone number must be 10 digits")
    @Max(value = 9999999999L, message = "Phone number must be 10 digits")
    private Long phoneNo;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
