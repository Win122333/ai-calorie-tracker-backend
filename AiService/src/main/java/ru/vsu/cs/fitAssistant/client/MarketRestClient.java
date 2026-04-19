package ru.vsu.cs.fitAssistant.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class MarketRestClient {
    private final RestClient restClient;

}
