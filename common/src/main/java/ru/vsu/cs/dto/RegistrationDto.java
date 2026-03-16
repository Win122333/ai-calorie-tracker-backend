package ru.vsu.cs.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record RegistrationDto(
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Пароль обязателен")
        @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
        String password,

        @NotBlank(message = "Имя обязательно")
        String firstName,

        String lastName,

        @NotNull(message = "Пол обязателен")
        Gender gender,

        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthDate,

        @Positive(message = "Вес должен быть больше 0")
        Double weight,

        @Positive(message = "Рост должен быть больше 0")
        Integer height,

        @NotNull(message = "Уровень активности обязателен")
        Double activityLevel,

        @NotNull(message = "Необходимо выбрать цель")
        Integer targetId // ID из таблицы t_target
) {}
