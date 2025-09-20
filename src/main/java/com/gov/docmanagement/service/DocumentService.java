package com.gov.docmanagement.service;

import com.gov.docmanagement.ApplicationConstants;
import com.gov.docmanagement.model.*;
import com.gov.docmanagement.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private DocumentStageRepository documentStageRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DocDownloadRequestRepository docDownloadRequestRepository;

    @Value("${dms.file-storage.base-dir}")
    String baseDir;

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    public Document saveDocument(Document document, MultipartFile file) throws  IOException {
        // Build folder path based on department/year/month/day
        String dept="";
        logger.info("Base Dir - "+baseDir);
        Optional<Department> department= departmentRepository.findById(document.getDept().getId());
        if(department.isPresent()) {
            logger.info("Department Found ");
            dept = department.get().getName();
            document.getDept().setName(dept);
        }else {
            throw new RuntimeException("Wrong Department Passed");
        }
        LocalDate today = LocalDate.now();

        Path folder = Paths.get(baseDir, dept,
                String.valueOf(today.getYear()),
                String.valueOf(today.getMonthValue()),
                String.valueOf(today.getDayOfMonth()));

        Files.createDirectories(folder); // will create if not exist

        // Save file with unique name
        String fileName = UUID.randomUUID() + ".pdf";
        Path filePath = folder.resolve(fileName);
        file.transferTo(filePath.toFile());

        document.setFile_path(filePath.toString());
        document.setCreatedDate(LocalDateTime.now());
        document.setStatus(ApplicationConstants.Uploaded);
        document.setVersion("1.0");

        Document savedDoc = documentRepository.save(document);

        // Create entry in document_stage_m
        Stage uploadedStage = stageRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Stage 1 (Uploaded) not found"));

        DocumentStage docStage = new DocumentStage();
        docStage.setDocument(savedDoc);
        docStage.setStage(uploadedStage);
        docStage.setChangedBy(savedDoc.getCreatedBy());
        documentStageRepository.save(docStage);

        return savedDoc;
    }

    public void save(Document document) {
        documentRepository.save(document);
    }

    public void moveDocumentToStage(Long documentId, Long stageId, String changedBy) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage not found"));

        // Update current stage in document
        document.setStatus(stage.getStagename());
        documentRepository.save(document);

        // Log in history table
        DocumentStage documentStage = new DocumentStage();
        documentStage.setDocument(document);
        documentStage.setStage(stage);
        documentStage.setChangedBy(changedBy);
        documentStageRepository.save(documentStage);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public List<DocumentStage> getDocumentStageHistory(Long documentId) {
        return documentStageRepository.findByDocumentId(documentId);
    }

    public List<Document> findByDepartmentAndStatus(Long departmentId, String status) {
        return documentRepository.findByDeptIdAndStatus(departmentId, status);
    }


    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    public Document findByRefNumber(String refNumber) {
        return documentRepository.findByrefnumber(refNumber)
                .orElseThrow(() -> new RuntimeException("Document not found with ref number: " + refNumber));
    }

    public Long numberOfDocumentsUploadedToday() {
        return documentRepository.countDocumentsUploadedToday();
    }

    public Long numberOfDocumentsUploadedThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        return documentRepository.countDocumentsUploadedThisWeek(startOfWeek.atStartOfDay(),
                endOfWeek.atTime(LocalTime.MAX));
    }

    public Long numberOfDocumentsUploadedThisMonth() {
        return documentRepository.countDocumentsUploadedThisMonth();
    }

    public Long numberOfDocumentsUploadedDateRange(LocalDate startDate, LocalDate endDate) {
        return documentRepository.countDocumentsUploadedBetween(startDate.atStartOfDay(),
                endDate.atTime(LocalTime.MAX));
    }

    public Resource downloadDocument(Long documentId) {
        Document doc = findById(documentId);
        Path filePath = Paths.get(doc.getFile_path());
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error loading file: " + doc.getFile_path());
        }
    }

    // ===================== Download Request Logic =====================

    public ResponseEntity<String> raiseDownloadRequest(Long documentId, Long userId) {
        Document doc = findById(documentId);
        if(null != doc) {
            DocDownloadRequest request = new DocDownloadRequest();
            request.setDocument(doc);
            request.setUserId(userId);
            request.setStatus(ApplicationConstants.RequestSent);
            docDownloadRequestRepository.save(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Document Download Request Sent to Head of the Department");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Document Not Found");
        }
    }

    public ResponseEntity<String> approveDownloadRequest(Long requestId, String hodName) {
        DocDownloadRequest req = docDownloadRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Download request not found"));

        if(null != req && ApplicationConstants.RequestSent.equalsIgnoreCase(req.getStatus()) ) {
            req.setStatus(ApplicationConstants.ApprovedDownload);
            req.setApprovedBy(hodName);
            req.setApprovedAt(LocalDateTime.now());
             docDownloadRequestRepository.save(req);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Document Download Request Approved!");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Document Already Approved or Rejected");
        }
    }

    public ResponseEntity<String> rejectDownloadRequest(Long requestId, String hodName) {
        DocDownloadRequest req = docDownloadRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Download request not found"));
        if(null != req && ApplicationConstants.RequestSent.equalsIgnoreCase(req.getStatus()) ) {
            req.setStatus(ApplicationConstants.Rejected);
            req.setApprovedBy(hodName);
            req.setApprovedAt(LocalDateTime.now());
            docDownloadRequestRepository.save(req);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Document Download Request Rejected!");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Document Download Request is Already Approved Or Rejected");
        }
    }

    public List<DocDownloadRequest> getPendingDownloadRequests() {
        return docDownloadRequestRepository.findByStatus(ApplicationConstants.RequestSent);
    }

    public Document saveDocument(Document document) {
        if (documentRepository.existsByRefnumber(document.getRefnumber())) {
            throw new IllegalArgumentException("Ref Number already exists!");
        }
        return documentRepository.save(document);
    }

    public ResponseEntity<DocDownloadRequest> checkStatusOfDownloadRequest(Long documentId, Long userId) {
        Document doc = findById(documentId);
        if(null != doc) {
            DocDownloadRequest request = docDownloadRequestRepository.findByDocumentIdAndUserId(documentId,userId);
            if(null != request) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(request);
            }else {
                throw new RuntimeException("Document Request Not Found");
            }
        }else {
            throw new RuntimeException("Document Not Found");
        }
    }
}

