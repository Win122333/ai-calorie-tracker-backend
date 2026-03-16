package ru.vsu.cs.fitAssistant.profile.entity;

public enum ActivityTarget {
    WEIGHT_GAIN("Набор веса"),
    WEIGHT_LOSS("Сброс веса"),
    WEIGHT_MAINTENANCE("Поддержание веса");


    private String description;
    ActivityTarget(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
