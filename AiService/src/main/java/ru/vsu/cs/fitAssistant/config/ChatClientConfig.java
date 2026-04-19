package ru.vsu.cs.fitAssistant.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для создания клиента взаимодействия с ИИ.
 */
@Configuration
public class ChatClientConfig {

    /**
     * Создает бин ChatClient, используя автоматически сконфигурированный Builder.
     * Этот клиент будет использовать ту модель, которую ты прописал в application.yml
     * (например, Google Gemini или OpenAI).
     *
     * @param builder бин-конструктор, предоставляемый Spring AI Starter.
     * @return готовый к работе клиент для отправки промптов.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}