package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "document_m")
@Data
public class Document {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String refNumber;
        private String financial_year;
        private String file_path;
        private String version;
        private Date createdDate;
        private String createdBy;
        private String status;// Approved, Hold,
        @OneToOne
        @JoinColumn(name = "department_id")
        private Department dept;

}
