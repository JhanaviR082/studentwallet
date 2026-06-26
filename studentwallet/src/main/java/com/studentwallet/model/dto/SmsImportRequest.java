package com.studentwallet.model.dto;

import jakarta.validation.constraints.NotBlank;

public class SmsImportRequest {

    @NotBlank(message = "SMS text is required")
    private String smsText;

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }
}
