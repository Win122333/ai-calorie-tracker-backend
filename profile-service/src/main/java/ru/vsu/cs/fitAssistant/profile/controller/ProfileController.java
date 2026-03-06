package ru.vsu.cs.fitAssistant.profile.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.dto.ResponseProfileDto;
import ru.vsu.cs.fitAssistant.profile.mapper.Mapper;
import ru.vsu.cs.fitAssistant.profile.service.ProfileService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final Mapper mapper;

    @GetMapping("/getAll")
    public ResponseEntity<List<ResponseProfileDto>> getAll() {
        log.info("called GET /users/profile");
        return ResponseEntity.ok(
                (profileService.getAll().stream().map(mapper::toResponse).toList()));
    }
    @GetMapping("/me")
    public ResponseEntity<ResponseProfileDto> getById(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID principalUUID = UUID.fromString(jwt.getSubject());
        log.info("called GET /users/profile/{}", principalUUID);
        return ResponseEntity.status(HttpStatus.FOUND).body(mapper.toResponse(
                profileService.getById(principalUUID).orElseThrow(() ->
                new NoSuchElementException("Ничего не найдено"))));
    }
}
