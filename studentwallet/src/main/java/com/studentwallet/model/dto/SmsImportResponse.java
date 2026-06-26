package com.studentwallet.model.dto;

import java.math.BigDecimal;

public class SmsImportResponse {

    private boolean parsed;
    private BigDecimal amount;
    private String category;
    private String merchant;
    private String description;
    private ExpenseResponse expense;
    private String message;

    public SmsImportResponse() {
    }

    public boolean isParsed() {
        return parsed;
    }

    public void setParsed(boolean parsed) {
        this.parsed = parsed;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseResponse getExpense() {
        return expense;
    }

    public void setExpense(ExpenseResponse expense) {
        this.expense = expense;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
