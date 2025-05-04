package com.example.studentportrait.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/dify")
public class DifyProxyController {
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/add-segment")
    public ResponseEntity<?> addSegment(@RequestBody Map<String, Object> req) {
        String apiKey = (String) req.get("apiKey");
        String datasetId = (String) req.get("datasetId");
        String documentId = (String) req.get("documentId");
        String content = (String) req.get("content");
        if (apiKey == null || datasetId == null || documentId == null || content == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "参数不完整"));
        }
        String url = String.format("https://api.dify.ai/v1/datasets/%s/documents/%s/segments", datasetId, documentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        Map<String, Object> body = new HashMap<>();
        body.put("segments", List.of(Map.of("content", content)));
        System.out.println("[DifyProxy] 请求URL: " + url);
        System.out.println("[DifyProxy] 请求Headers: " + headers);
        System.out.println("[DifyProxy] 请求Body: " + body);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            String responseBody = e.getResponseBodyAsString();
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", "代理Dify API失败: " + e.getMessage(), "difyResponse", responseBody));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "代理Dify API失败: " + e.getMessage()));
        }
    }
} 