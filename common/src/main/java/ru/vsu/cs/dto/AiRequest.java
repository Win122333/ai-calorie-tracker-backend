package ru.vsu.cs.dto;

import java.time.LocalDate;

public record AiRequest (
    Gender gender,
    LocalDate birthDate,
    Double weight,
    Integer height,
    Double activityLevel,
    Integer targetId,
    Double weeklyBudget
) {
}
