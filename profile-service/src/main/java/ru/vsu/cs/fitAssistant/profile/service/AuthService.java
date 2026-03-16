package ru.vsu.cs.fitAssistant.profile.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.vsu.cs.dto.LoginDto;
import ru.vsu.cs.dto.RegistrationDto;
import ru.vsu.cs.fitAssistant.profile.entity.Gender;
import ru.vsu.cs.fitAssistant.profile.entity.ProfileEntity;
import ru.vsu.cs.fitAssistant.profile.entity.TargetEntity;
import ru.vsu.cs.fitAssistant.profile.entity.TokenResponseDto;
import ru.vsu.cs.fitAssistant.profile.repository.ProfileRepository;
import ru.vsu.cs.fitAssistant.profile.repository.TargetRepository;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final Keycloak keycloak; // Тот самый бин из конфига выше
    private final ProfileRepository profileRepository;
    private final TargetRepository targetRepository;

    @Value("${keycloak.realm}")
    private String realm;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri; // Например, http://localhost:5442/realms/FitAssistant

    @Value("${keycloak.client-id}")
    private String clientId; // profile-app

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @Transactional
    public void register(RegistrationDto dto) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(dto.email());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.password());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        Response response = keycloak.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            log.error("Keycloak error: {}", response.getStatusInfo().getReasonPhrase());
            throw new RuntimeException("Не удалось создать пользователя в Keycloak");
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        ProfileEntity profile = new ProfileEntity();
        profile.setId(UUID.fromString(userId));
        profile.setName(dto.firstName());
        profile.setSurname(dto.lastName());
        profile.setWeight(dto.weight());
        profile.setHeight(dto.height());
        profile.setBirthday(dto.birthDate());
        profile.setGender(Gender.valueOf(dto.gender().name()));
        profile.setActivityLevel(dto.activityLevel());

        TargetEntity target = targetRepository.findById(dto.targetId())
                .orElseThrow(() -> new RuntimeException("Цель не найдена"));
        profile.setTarget(target);

        profileRepository.save(profile);
        log.info("Пользователь {} успешно зарегистрирован с ID {}", dto.email(), userId);
    }
    public TokenResponseDto login(LoginDto dto) {
        String tokenUrl = issuerUri + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", dto.email());
        body.add("password", dto.password());
        body.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            log.info("Attempting login for user: {}", dto.email());
            ResponseEntity<TokenResponseDto> response = restTemplate.postForEntity(
                    tokenUrl,
                    request,
                    TokenResponseDto.class
            );

            return response.getBody();

        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.BadRequest e) {
            log.error("Login failed for user {}: {}", dto.email(), e.getMessage());
            throw new BadCredentialsException("Неверный логин или пароль");
        } catch (Exception e) {
            log.error("Unexpected error during login: ", e);
            throw new RuntimeException("Сервис авторизации временно недоступен");
        }
    }
}