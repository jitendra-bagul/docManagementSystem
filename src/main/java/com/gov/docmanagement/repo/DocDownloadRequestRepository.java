package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.DocDownloadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocDownloadRequestRepository extends JpaRepository<DocDownloadRequest, Long> {
    List<DocDownloadRequest> findByUserId(Long userId);

    List<DocDownloadRequest> findByStatus(String status);

    List<DocDownloadRequest> findByDocumentId(Long documentId);

    DocDownloadRequest findByDocumentIdAndUserId(Long documentId,Long userId);
}