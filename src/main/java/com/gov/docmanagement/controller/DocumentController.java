package com.gov.docmanagement.controller;

import com.gov.docmanagement.model.Document;
import com.gov.docmanagement.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    //uploadDocument();
    //searchDocument();
    //numberOfDocumentsUploadedToday();

    @Autowired
    private DocumentService documentService;

    // Upload document
    @PostMapping("/upload")
    public String uploadDocument(@RequestBody Document document) {
        documentService.save(document);
        return "Document uploaded successfully";
    }

    // View all documents
    @GetMapping("/all")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    // Approve or Send Back Document
    @PostMapping("/approveOrSendBack")
    public String approveOrSendBack(@RequestParam("documentId") Long documentId, @RequestParam("status") String status) {
        documentService.updateDocumentStatus(documentId, status);
        return "Document status updated";
    }


}
