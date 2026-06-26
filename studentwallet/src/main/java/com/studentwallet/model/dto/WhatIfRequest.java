package com.studentwallet.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class WhatIfRequest {

    @NotNull
    @DecimalMin(value = "1.0", message = "Savings goal must be at least ₹1")
    private BigDecimal extraSavingsGoal;

    public BigDecimal getExtraSavingsGoal() {
        return extraSavingsGoal;
    }

    public void setExtraSavingsGoal(BigDecimal extraSavingsGoal) {
        this.extraSavingsGoal = extraSavingsGoal;
    }
}
