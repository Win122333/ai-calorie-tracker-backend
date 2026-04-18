package ru.vsu.cs.dto;

public record ErrorResponseDto(
        Integer statusCode,
        String message
) {
}
