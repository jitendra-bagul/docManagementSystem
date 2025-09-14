package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByDeptIdAndStatus(Long departmentId, String status);
}
