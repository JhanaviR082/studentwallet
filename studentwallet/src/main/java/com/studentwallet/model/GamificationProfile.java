package com.studentwallet.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table("gamification_profiles")
public class GamificationProfile {

    @PrimaryKey
    @Column("user_id")
    private String userId;

    @Column("display_name")
    private String displayName;

    @Column("current_streak")
    private int currentStreak;

    @Column("longest_streak")
    private int longestStreak;

    @Column("last_under_budget_date")
    private LocalDate lastUnderBudgetDate;

    @Column("earned_badges")
    private List<String> earnedBadges;

    @Column("savings_vault")
    private BigDecimal savingsVault;

    @Column("total_daily_savings")
    private BigDecimal totalDailySavings;

    public GamificationProfile() {
        this.earnedBadges = new ArrayList<>();
        this.savingsVault = BigDecimal.ZERO;
        this.totalDailySavings = BigDecimal.ZERO;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public LocalDate getLastUnderBudgetDate() {
        return lastUnderBudgetDate;
    }

    public void setLastUnderBudgetDate(LocalDate lastUnderBudgetDate) {
        this.lastUnderBudgetDate = lastUnderBudgetDate;
    }

    public List<String> getEarnedBadges() {
        return earnedBadges;
    }

    public void setEarnedBadges(List<String> earnedBadges) {
        this.earnedBadges = earnedBadges;
    }

    public BigDecimal getSavingsVault() {
        return savingsVault;
    }

    public void setSavingsVault(BigDecimal savingsVault) {
        this.savingsVault = savingsVault;
    }

    public BigDecimal getTotalDailySavings() {
        return totalDailySavings;
    }

    public void setTotalDailySavings(BigDecimal totalDailySavings) {
        this.totalDailySavings = totalDailySavings;
    }
}
