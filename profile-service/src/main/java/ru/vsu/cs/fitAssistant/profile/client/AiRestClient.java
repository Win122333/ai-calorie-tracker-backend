package ru.vsu.cs.fitAssistant.profile.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.vsu.cs.dto.AiRequest;
import ru.vsu.cs.dto.AiResponse;
import ru.vsu.cs.fitAssistant.profile.util.ApiPath;

import java.util.List;

/**
 * HTTP-клиент для интеграции с микросервисом генерации AI-ответов.
 * Отвечает за отправку параметров пользователя и получение сгенерированных вариантов меню (рациона).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AiRestClient {

    /**
     * Type reference для безопасной десериализации списка объектов AiResponse из JSON.
     */
    private static final ParameterizedTypeReference<List<AiResponse>> OFFERS_TYPE_REFERENCES =
            new ParameterizedTypeReference<>() {};

    private final RestClient restClient;

    /**
     * Отправляет POST-запрос в AI микросервис для генерации плана питания.
     *
     * @param request DTO с входными данными для нейросети (параметры тела, бюджет, предпочтения).
     * @return Список сгенерированных вариантов меню ({@link AiResponse}).
     * @throws org.springframework.web.client.RestClientException в случае сетевых ошибок или ответов 4xx/5xx от AI сервиса.
     */
    public List<AiResponse> generateAiResponse(AiRequest request) {
        log.info("Отправка запроса в МС AiService ({}) для расчета предложений", ApiPath.AI_PATH);
        return restClient
                .post()
                .uri(ApiPath.AI_PATH)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(OFFERS_TYPE_REFERENCES);
    }
}