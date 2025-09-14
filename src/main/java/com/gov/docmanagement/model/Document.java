package com.gov.docmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "document_m")
@Data
public class Document {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message="Title required")
        private String title;
        @NotBlank(message="Ref Number required")
        private String refnumber;
        @NotBlank(message="Financial Year required")
        private String financial_year;
        private String file_path;
        private String version;
        private Date createdDate;
        @NotBlank(message="Document Uploaded By is Required")
        private String createdBy;
        private String status;// Approved, Hold,
        @OneToOne
        @JoinColumn(name = "department_id")
        private Department dept;
        @NotBlank(message="Document Type required")
        private String type;

}
