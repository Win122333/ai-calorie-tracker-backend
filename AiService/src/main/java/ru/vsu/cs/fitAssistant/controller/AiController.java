package ru.vsu.cs.fitAssistant.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.dto.AiRequest;
import ru.vsu.cs.dto.AiResponse;
import ru.vsu.cs.fitAssistant.service.AiService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;
    @PostMapping("/generate")
    public ResponseEntity<List<AiResponse>> generate(
            @RequestBody AiRequest request
    ) {
        return ResponseEntity.ok(aiService.generateMenu(request));
    }
}
