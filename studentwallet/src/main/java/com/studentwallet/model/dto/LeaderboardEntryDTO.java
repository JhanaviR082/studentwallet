package com.studentwallet.model.dto;

public class LeaderboardEntryDTO {

    private String userId;
    private String displayName;
    private int budgetAdherencePercent;
    private int currentStreak;
    private int rank;
    private boolean currentUser;

    public LeaderboardEntryDTO() {
    }

    public LeaderboardEntryDTO(String userId, String displayName, int budgetAdherencePercent,
                               int currentStreak, int rank, boolean currentUser) {
        this.userId = userId;
        this.displayName = displayName;
        this.budgetAdherencePercent = budgetAdherencePercent;
        this.currentStreak = currentStreak;
        this.rank = rank;
        this.currentUser = currentUser;
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

    public int getBudgetAdherencePercent() {
        return budgetAdherencePercent;
    }

    public void setBudgetAdherencePercent(int budgetAdherencePercent) {
        this.budgetAdherencePercent = budgetAdherencePercent;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }
}
