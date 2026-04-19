package ru.vsu.cs.fitAssistant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient getRestClient(
            @Value("${market.base-url:http://localhost:5445}") String baseUrl,
            RestClient.Builder builder
    ) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }
}
