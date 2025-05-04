package com.example.studentportrait.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class ChatController {
    @Value("${dify.api-key}")
    private String difyApiKey;

    @Value("${dify.base-url}")
    private String difyBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        String userId = request.getOrDefault("user", UUID.randomUUID().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + difyApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", new HashMap<>());
        body.put("query", query);
        body.put("user", userId);
        body.put("response_mode", "blocking");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    difyBaseUrl + "/chat-messages",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to communicate with Dify API: " + e.getMessage()));
        }
    }
} 