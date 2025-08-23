package com.gov.docmanagement.service;

import com.gov.docmanagement.model.Document;
import com.gov.docmanagement.repo.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public void save(Document document) {
        documentRepository.save(document);
    }

    public void updateDocumentStatus(Long documentId, String status) {
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new RuntimeException("Document not found"));
        document.setStatus(status);
        documentRepository.save(document);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

}
