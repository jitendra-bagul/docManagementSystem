package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.DocumentStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentStageRepository extends JpaRepository<DocumentStage, Long> {
    List<DocumentStage> findByDocumentId(Long documentId);
}
