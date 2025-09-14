package com.gov.docmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class DocumentHistoryResponse {

    private Long documentId;
    private String title;
    private String refNumber;
    private String financialYear;
    private String status;
    private String createdBy;
    private String filePath;

    private List<StageHistory> stageHistory;

    @Data
    @AllArgsConstructor
    public static class StageHistory {
        private String stage;
        private LocalDateTime changedOn;
        private String changedBy;
    }
}
