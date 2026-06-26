package com.studentwallet.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class WhatIfResponse {

    private BigDecimal currentDailyBudget;
    private BigDecimal adjustedDailyBudget;
    private BigDecimal dailyReduction;
    private BigDecimal extraSavingsGoal;
    private long daysRemaining;
    private List<String> tips;

    public WhatIfResponse() {
    }

    public WhatIfResponse(BigDecimal currentDailyBudget, BigDecimal adjustedDailyBudget,
                          BigDecimal dailyReduction, BigDecimal extraSavingsGoal,
                          long daysRemaining, List<String> tips) {
        this.currentDailyBudget = currentDailyBudget;
        this.adjustedDailyBudget = adjustedDailyBudget;
        this.dailyReduction = dailyReduction;
        this.extraSavingsGoal = extraSavingsGoal;
        this.daysRemaining = daysRemaining;
        this.tips = tips;
    }

    public BigDecimal getCurrentDailyBudget() {
        return currentDailyBudget;
    }

    public void setCurrentDailyBudget(BigDecimal currentDailyBudget) {
        this.currentDailyBudget = currentDailyBudget;
    }

    public BigDecimal getAdjustedDailyBudget() {
        return adjustedDailyBudget;
    }

    public void setAdjustedDailyBudget(BigDecimal adjustedDailyBudget) {
        this.adjustedDailyBudget = adjustedDailyBudget;
    }

    public BigDecimal getDailyReduction() {
        return dailyReduction;
    }

    public void setDailyReduction(BigDecimal dailyReduction) {
        this.dailyReduction = dailyReduction;
    }

    public BigDecimal getExtraSavingsGoal() {
        return extraSavingsGoal;
    }

    public void setExtraSavingsGoal(BigDecimal extraSavingsGoal) {
        this.extraSavingsGoal = extraSavingsGoal;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public List<String> getTips() {
        return tips;
    }

    public void setTips(List<String> tips) {
        this.tips = tips;
    }
}
