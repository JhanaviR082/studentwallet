package com.studentwallet.model.dto;

import java.math.BigDecimal;

public class BudgetDashboardDTO {

    private BigDecimal dailyAllowance;
    private BigDecimal remainingForToday;
    private BigDecimal comingDaysAllowance;
    private BigDecimal monthEndSurplus;
    private BigDecimal potentialSavings;
    private BigDecimal spentToday;
    private BigDecimal totalSpent;

    public BudgetDashboardDTO() {
    }

    public BudgetDashboardDTO(BigDecimal dailyAllowance, BigDecimal remainingForToday,
                              BigDecimal comingDaysAllowance, BigDecimal monthEndSurplus,
                              BigDecimal potentialSavings, BigDecimal spentToday,
                              BigDecimal totalSpent) {
        this.dailyAllowance = dailyAllowance;
        this.remainingForToday = remainingForToday;
        this.comingDaysAllowance = comingDaysAllowance;
        this.monthEndSurplus = monthEndSurplus;
        this.potentialSavings = potentialSavings;
        this.spentToday = spentToday;
        this.totalSpent = totalSpent;
    }

    public BigDecimal getDailyAllowance() {
        return dailyAllowance;
    }

    public void setDailyAllowance(BigDecimal dailyAllowance) {
        this.dailyAllowance = dailyAllowance;
    }

    public BigDecimal getRemainingForToday() {
        return remainingForToday;
    }

    public void setRemainingForToday(BigDecimal remainingForToday) {
        this.remainingForToday = remainingForToday;
    }

    public BigDecimal getComingDaysAllowance() {
        return comingDaysAllowance;
    }

    public void setComingDaysAllowance(BigDecimal comingDaysAllowance) {
        this.comingDaysAllowance = comingDaysAllowance;
    }

    public BigDecimal getMonthEndSurplus() {
        return monthEndSurplus;
    }

    public void setMonthEndSurplus(BigDecimal monthEndSurplus) {
        this.monthEndSurplus = monthEndSurplus;
    }

    public BigDecimal getPotentialSavings() {
        return potentialSavings;
    }

    public void setPotentialSavings(BigDecimal potentialSavings) {
        this.potentialSavings = potentialSavings;
    }

    public BigDecimal getSpentToday() {
        return spentToday;
    }

    public void setSpentToday(BigDecimal spentToday) {
        this.spentToday = spentToday;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}
