package com.gov.docmanagement.config.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String username;
    private String newPassword;
}