package ru.vsu.cs.dto;

import java.util.List;

public record AiResponse(
        List<String> products,
        Double calories,
        Double protein,
        Double fats,
        Double carbohydrates,
        Double cost
) {
}
