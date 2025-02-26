package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getFoodRecommendation(String ingredients) {
        // 📌 GPT 프롬프트 생성
        String prompt = String.format(
                "당신은 음식 추천 전문가입니다. 사용자가 선택한 재료는 %s입니다. " +
                        "이 정보를 바탕으로 사용자가 만들기 좋은 요리를 추천해주세요. " +
                        "친근한 톤으로 2~3개의 추천 메뉴를 제공하고, 이유도 설명해주세요.",
                ingredients
        );

        // 📌 OpenAI API 요청 데이터 설정
        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4");
        request.put("messages", List.of(
                Map.of("role", "system", "content", "당신은 음식 추천 전문가입니다."),
                Map.of("role", "user", "content", prompt)
        ));
        request.put("temperature", 0.7);

        // 📌 HTTP 요청 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, Map.class);

        // 📌 응답 데이터 파싱
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        return (String) ((Map<String, Object>) choices.get(0).get("message")).get("content");
    }
}