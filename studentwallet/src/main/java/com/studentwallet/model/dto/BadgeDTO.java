package com.studentwallet.model.dto;

public class BadgeDTO {

    private String id;
    private String name;
    private String description;
    private String icon;
    private boolean earned;
    private int progress;
    private int target;
    private int bonusAmount;

    public BadgeDTO() {
    }

    public BadgeDTO(String id, String name, String description, String icon,
                    boolean earned, int progress, int target, int bonusAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.earned = earned;
        this.progress = progress;
        this.target = target;
        this.bonusAmount = bonusAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isEarned() {
        return earned;
    }

    public void setEarned(boolean earned) {
        this.earned = earned;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(int bonusAmount) {
        this.bonusAmount = bonusAmount;
    }
}
