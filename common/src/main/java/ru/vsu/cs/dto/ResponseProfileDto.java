package ru.vsu.cs.dto;

public record ResponseProfileDto (
        String name,
        String surname,
        Double weight,
        Integer height,
        Double weeklyBudget,
        Double activityLevel
) {
}
