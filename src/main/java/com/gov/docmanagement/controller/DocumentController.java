package com.gov.docmanagement.controller;

import com.gov.docmanagement.model.*;
import com.gov.docmanagement.repo.DepartmentRepository;
import com.gov.docmanagement.repo.StageRepository;
import com.gov.docmanagement.service.DocumentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    //searchDocument();
    //numberOfDocumentsUploadedToday();
    //view()
    //raiseDownloadRequest()- will send request to HOD, if approved by HOD then user can download, later in UI will see Doownload option
    //download()- if office user, can download without approvals

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DocumentService documentService;

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @Valid @RequestPart("document") Document document,
            BindingResult result,
            @RequestPart("file") MultipartFile file) throws IOException {

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }

        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are allowed");
        }

        Document savedDoc = documentService.saveDocument(document, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoc);
    }
    // View all documents
    @GetMapping("/all")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveDocument(
            @RequestParam Long documentId,
            @RequestParam String stage,
            @RequestParam String updatedBy) {

        // Find stage by name
        Stage targetStage = stageRepository.findByStagename(stage)
                .orElseThrow(() -> new RuntimeException("Invalid stage: " + stage));

        documentService.moveDocumentToStage(documentId, targetStage.getId(), updatedBy);
        return ResponseEntity.ok(Map.of("message", "Document moved to stage: " + stage));
    }

    @PostMapping("/sendBack")
    public ResponseEntity<?> sendBackDocument(
            @RequestParam Long documentId,
            @RequestParam String stage,
            @RequestParam String updatedBy) {

        // Send back can simply move to previous stage
        Stage targetStage = stageRepository.findByStagename(stage)
                .orElseThrow(() -> new RuntimeException("Invalid stage: " + stage));

        documentService.moveDocumentToStage(documentId, targetStage.getId(), updatedBy);
        return ResponseEntity.ok(Map.of("message", "Document sent back to stage: " + stage));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<DocumentHistoryResponse> getDocumentHistory(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        //List<DocumentStage> stages = documentService.getDocumentStageHistory(id);
        List<DocumentStage> stages = documentService.getDocumentStageHistory(id)
                .stream()
                .sorted(Comparator.comparing(DocumentStage::getChangedOn))
                .toList();

        List<DocumentHistoryResponse.StageHistory> stageHistory = stages.stream()
                .map(s -> new DocumentHistoryResponse.StageHistory(
                        s.getStage().getStagename(),
                        s.getChangedOn(),
                        s.getChangedBy()
                ))
                .toList();

        DocumentHistoryResponse response = new DocumentHistoryResponse(
                document.getId(),
                document.getTitle(),
                document.getRefnumber(),
                document.getFinancial_year(),
                document.getStatus(),
                document.getCreatedBy(),
                document.getFile_path(),
                stageHistory
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterDocuments(
            @RequestParam String department,
            @RequestParam String status) {

        Department departmentObj = departmentRepository.findByName(department);
        List<Document> documents = documentService.findByDepartmentAndStatus(departmentObj.getId(), status);

        return ResponseEntity.ok(documents);
    }


}
