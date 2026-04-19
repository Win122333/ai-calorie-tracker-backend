package ru.vsu.cs.fitAssistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import ru.vsu.cs.dto.AiRequest;
import ru.vsu.cs.dto.AiResponse;
import ru.vsu.cs.fitAssistant.client.MarketRestClient;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiService {

    private final MarketRestClient marketRestClient;
    private final ChatClient chatClient;

    public List<AiResponse> generateMenu(AiRequest request) {
        log.info("Начинаем генерацию меню для пользователя. ID цели: {}", request.targetId());
        int age = Period.between(request.birthDate(), LocalDate.now()).getYears();
        double dailyBudget = request.weeklyBudget() / 7.0;
        String targetDescription = switch (request.targetId()) {
            case 1 -> "Похудение (дефицит калорий 15-20%%)";
            case 2 -> "Поддержание веса";
            case 3 -> "Набор мышечной массы (профицит калорий 15%%)";
            default -> "Поддержание здорового образа жизни";
        };

        String genderStr = request.gender().name().equalsIgnoreCase("MALE") ? "Мужчина" : "Женщина";

        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<AiResponse>>() {});

        String jsonExample = """
                [
                  {
                    "products": ["Название продукта 1 (вес в граммах)", "Название продукта 2 (вес в граммах)"],
                    "calories": 2000.5,
                    "protein": 150.0,
                    "fats": 70.0,
                    "carbohydrates": 200.0,
                    "cost": 500.0
                  }
                ]
                """;

        String systemPromptText = """
            Ты — профессиональный диетолог. Твоя задача — составить 3 различных варианта дневного рациона питания, опираясь на физиологические параметры пользователя, его цель и финансовые возможности.
                    
            ПАРАМЕТРЫ ПОЛЬЗОВАТЕЛЯ:
            - Пол: %s
            - Возраст: %d лет
            - Вес: %s кг
            - Рост: %d см
            - Коэффициент активности: %s
            - Цель: %s
            - Дневной бюджет: %s руб.
            
            ИНСТРУКЦИЯ ПО РАСЧЕТУ:
            1. Рассчитай базовый уровень метаболизма (BMR) по формуле Миффлина-Сан Жеора.
            2. Умножь BMR на коэффициент активности.
            3. Скорректируй итоговую калорийность согласно цели пользователя.
            4. Распредели макронутриенты (БЖУ) в адекватных пропорциях.
            5. Подбери продукты для каждого варианта рациона так, чтобы их суммарная стоимость не превышала дневной бюджет.
            
            ТРЕБОВАНИЯ К ФОРМАТУ ОТВЕТА (КРИТИЧЕСКИ ВАЖНО):
            Ты должен вернуть ТОЛЬКО валидный JSON-массив, содержащий ровно 3 объекта. Не пиши никакого сопроводительного текста до или после JSON.
            
            Структура каждого объекта должна строго соответствовать следующему формату:
            %s
            
            ПРАВИЛА ТИПОВ ДАННЫХ:
            - Значения calories, protein, fats, carbohydrates и cost должны быть ЧИСЛАМИ. Запрещено добавлять к ним текст вроде "ккал", "г" или "руб".
            - Поле products — это массив строк. Граммовки указывай прямо внутри строки с названием продукта. calories, protein, fats, carbohydrates и cost должны быть рассчитаны на один прием пищи
            
            СТРОГО СЛЕДУЙ ЭТОЙ СХЕМЕ:
            %s
            """.formatted(
                genderStr, age, request.weight(), request.height(), request.activityLevel(),
                targetDescription, dailyBudget, jsonExample, converter.getFormat()
        );

        log.debug("Отправка запроса в LLM. Возраст: {}, Бюджет на день: {}", age, dailyBudget);
        String responseContent = chatClient.prompt()
                .messages(
                        new SystemMessage(systemPromptText),
                        new UserMessage("Сгенерируй 3 различных варианта рациона.")
                )
                .call()
                .content();

        log.info("Ответ от LLM успешно получен. Парсим JSON...");
        return converter.convert(responseContent);
    }
}