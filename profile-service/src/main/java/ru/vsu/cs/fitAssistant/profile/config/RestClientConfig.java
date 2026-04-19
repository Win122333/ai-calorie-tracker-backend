package ru.vsu.cs.fitAssistant.profile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient getRestClient(
            @Value("${ai.base-url:http://localhost:5444}") String baseUrl,
            RestClient.Builder builder
    ) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}
