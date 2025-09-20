package com.gov.docmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "doc_download_request")
@Data
public class DocDownloadRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private Long userId;

    @Column(nullable = false)
    private String status;  // RequestSent, ApprovedDownload, Rejected

    private String approvedBy;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    @PrePersist
    public void prePersist() {
        requestedAt = LocalDateTime.now();
    }

    // Getters & Setters
}

