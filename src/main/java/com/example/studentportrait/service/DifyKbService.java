package com.example.studentportrait.service;

import com.example.studentportrait.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DifyKbService {
    @Value("${dify.api-key}")
    private String difyApiKey;

    @Value("${dify.base-url}")
    private String difyBaseUrl;

    @Value("${dify.dataset-id}")
    private String datasetId;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 创建Dify知识库文档
     */
    public String createDocumentInDify(Student student) {
        String url = String.format("%s/datasets/%s/document/create-by-text", difyBaseUrl, datasetId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + difyApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("name", student.getName() + " (" + student.getStudentId() + ")");
        body.put("text", formatStudentText(student));
        body.put("indexing_technique", "high_quality");
        body.put("process_rule", Map.of("mode", "automatic"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object docObj = response.getBody().get("document");
                if (docObj instanceof Map docMap && docMap.get("id") != null) {
                    return docMap.get("id").toString();
                }
            }
            log.error("Dify文档创建失败: {}", response.getBody());
        } catch (Exception e) {
            log.error("调用Dify创建文档异常", e);
        }
        return null;
    }

    /**
     * 更新Dify知识库文档
     */
    public void updateDocumentInDify(Student student) {
        if (student.getDifyDocumentId() == null || student.getDifyDocumentId().isEmpty()) {
            log.error("无法更新Dify文档，difyDocumentId为空");
            return;
        }
        String url = String.format("%s/datasets/%s/documents/%s/update-by-text", difyBaseUrl, datasetId, student.getDifyDocumentId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + difyApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("text", formatStudentText(student));
        body.put("name", student.getName() + " (" + student.getStudentId() + ")");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Dify文档更新失败: {}", response.getBody());
            }
        } catch (Exception e) {
            log.error("调用Dify更新文档异常", e);
        }
    }

    /**
     * 删除Dify知识库文档
     */
    public void deleteDocumentFromDify(String difyDocumentId) {
        if (difyDocumentId == null || difyDocumentId.isEmpty()) {
            log.error("无法删除Dify文档，difyDocumentId为空");
            return;
        }
        String url = String.format("%s/datasets/%s/documents/%s", difyBaseUrl, datasetId, difyDocumentId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + difyApiKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Map.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Dify文档删除失败: {}", response.getBody());
            }
        } catch (Exception e) {
            log.error("调用Dify删除文档异常", e);
        }
    }

    /**
     * 学生信息格式化为文本
     */
    private String formatStudentText(Student student) {
        return String.format("{" +
                "\n  \"学号\": \"%s\"," +
                "\n  \"姓名\": \"%s\"," +
                "\n  \"院系\": \"%s\"," +
                "\n  \"专业\": \"%s\"," +
                "\n  \"年级\": \"%s\"\n}",
                student.getStudentId(),
                student.getName(),
                student.getDepartment(),
                student.getMajor(),
                student.getGrade()
        );
    }
} 