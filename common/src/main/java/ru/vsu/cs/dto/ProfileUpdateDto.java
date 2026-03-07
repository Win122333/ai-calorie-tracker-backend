package ru.vsu.cs.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ProfileUpdateDto (

        Double weight,

        Integer height,

        Double activityLevel,

        Integer targetId
){
}
