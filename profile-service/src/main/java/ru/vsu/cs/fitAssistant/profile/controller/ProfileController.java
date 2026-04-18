package ru.vsu.cs.fitAssistant.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.dto.AiResponse;
import ru.vsu.cs.dto.ProfileUpdateDto;
import ru.vsu.cs.dto.ResponseProfileDto;
import ru.vsu.cs.fitAssistant.profile.mapper.Mapper;
import ru.vsu.cs.fitAssistant.profile.service.ProfileService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST-контроллер для управления профилями пользователей и запуска AI-генерации.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
@Tag(name = "Профиль пользователя", description = "API для управления данными пользователя и генерации меню")
public class ProfileController {

    private final ProfileService profileService;
    private final Mapper mapper;

    @Operation(summary = "Получить все профили", description = "Возвращает список всех профилей в системе (Требуются права администратора).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<ResponseProfileDto>> getAll() {
        log.info("called GET /api/v1/profile/getAll");
        return ResponseEntity.ok(
                profileService.getAll().stream().map(mapper::toResponse).toList()
        );
    }

    @Operation(summary = "Получить свой профиль", description = "Возвращает профиль текущего авторизованного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public ResponseEntity<ResponseProfileDto> getById(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        UUID principalUUID = UUID.fromString(jwt.getSubject());
        log.info("called GET /api/v1/profile/me for user: {}", principalUUID);

        return ResponseEntity.ok(mapper.toResponse(
                profileService.getById(principalUUID).orElseThrow(() ->
                        new NoSuchElementException("Профиль не найден"))));
    }

    @Operation(summary = "Обновить свой профиль", description = "Частичное обновление данных текущего пользователя (вес, бюджет, цели и т.д.).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/me")
    public ResponseEntity<ResponseProfileDto> update(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt,
            @RequestBody ProfileUpdateDto dto
    ) {
        UUID principalUUID = UUID.fromString(jwt.getSubject());
        log.info("called PATCH /api/v1/profile/me with dto: {}", dto);

        return ResponseEntity.ok(mapper.toResponse(
                profileService.update(principalUUID, dto)));
    }

    @Operation(summary = "Сгенерировать рацион", description = "Запускает генерацию меню питания на основе параметров и бюджета текущего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Меню успешно сгенерировано"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка внешнего AI сервиса")
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/generate")
    public ResponseEntity<AiResponse> generateAiResponse(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    ) {
        UUID principalUUID = UUID.fromString(jwt.getSubject());
        log.info("called POST /api/v1/profile/generate for user: {}", principalUUID);

        return ResponseEntity.ok(profileService.generateAiResponse(principalUUID));
    }
}