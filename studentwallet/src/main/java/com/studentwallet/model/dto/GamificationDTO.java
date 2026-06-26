package com.studentwallet.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class GamificationDTO {

    private int currentStreak;
    private int longestStreak;
    private List<BadgeDTO> badges;
    private String displayName;
    private BigDecimal savingsVault;
    private BigDecimal totalDailySavings;
    private BigDecimal todaySaved;

    public GamificationDTO() {
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

    public List<BadgeDTO> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeDTO> badges) {
        this.badges = badges;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public BigDecimal getTodaySaved() {
        return todaySaved;
    }

    public void setTodaySaved(BigDecimal todaySaved) {
        this.todaySaved = todaySaved;
    }
}
