package com.example.controller;

import com.example.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*") // 프론트엔드 연동을 위해 CORS 설정
public class ChatbotController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping
    public Map<String, String> chatWithBot(@RequestBody Map<String, String> request) {
        String ingredients = request.get("ingredients");
        String botResponse = openAIService.getFoodRecommendation(ingredients);

        return Map.of("response", botResponse);
    }
}
