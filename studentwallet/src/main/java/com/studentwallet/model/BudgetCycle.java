package com.studentwallet.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("budget_cycles")
public class BudgetCycle {

    @PrimaryKey
    @Column("cycle_id")
    private UUID cycleId;

    @Column("user_id")
    private String userId;

    @Column("start_date")
    private LocalDate startDate;

    @Column("end_date")
    private LocalDate endDate;

    @Column("total_income")
    private BigDecimal totalIncome;

    @Column("daily_base_budget")
    private BigDecimal dailyBaseBudget;

    @Column("cycle_name")
    private String cycleName;

    public BudgetCycle() {
    }

    public UUID getCycleId() {
        return cycleId;
    }

    public void setCycleId(UUID cycleId) {
        this.cycleId = cycleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getDailyBaseBudget() {
        return dailyBaseBudget;
    }

    public void setDailyBaseBudget(BigDecimal dailyBaseBudget) {
        this.dailyBaseBudget = dailyBaseBudget;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }
}
