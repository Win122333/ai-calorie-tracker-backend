package ru.vsu.cs.dto;

import java.time.LocalDate;

public record ProfileRegistrationDto(
        String name,
        String surname,
        Gender gender,
        LocalDate birthday,
        Double weight,
        Integer height,
        Double activityLevel,
        Integer targetId
) {}