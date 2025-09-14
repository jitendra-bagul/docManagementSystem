package com.gov.docmanagement.service;

import com.gov.docmanagement.ApplicationConstants;
import com.gov.docmanagement.model.Department;
import com.gov.docmanagement.model.Document;
import com.gov.docmanagement.model.DocumentStage;
import com.gov.docmanagement.model.Stage;
import com.gov.docmanagement.repo.DepartmentRepository;
import com.gov.docmanagement.repo.DocumentRepository;
import com.gov.docmanagement.repo.DocumentStageRepository;
import com.gov.docmanagement.repo.StageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
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
        document.setCreatedDate(Date.valueOf(today));
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

}
