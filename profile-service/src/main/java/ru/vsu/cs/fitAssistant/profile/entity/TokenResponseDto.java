package ru.vsu.cs.fitAssistant.profile.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponseDto(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("expires_in")
        long expiresIn,

        @JsonProperty("refresh_expires_in")
        long refreshExpiresIn,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("scope")
        String scope
) {}
