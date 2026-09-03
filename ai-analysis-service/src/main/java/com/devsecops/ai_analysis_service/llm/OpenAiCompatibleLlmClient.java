package com.devsecops.ai_analysis_service.llm;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenAiCompatibleLlmClient implements LlmClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiCompatibleLlmClient.class);

    @Value("${llm.endpoint}")
    private String llmEndpoint;

    @Value("${llm.model}")
    private String llmModel;

    @Value("${llm.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final FallbackExplainer fallbackExplainer;

    public OpenAiCompatibleLlmClient(FallbackExplainer fallbackExplainer) {
        this.restTemplate = new RestTemplate();
        this.fallbackExplainer = fallbackExplainer;
    }

    @Override
    public LlmResult explain(String contextPayload, JsonNode findings) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (apiKey != null && !apiKey.isBlank()) {
                headers.setBearerAuth(apiKey);
            }

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", llmModel);
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", "You are an expert security analyst. Review the provided code context and findings. Summarize the vulnerabilities, explain their impact, and provide a single risk score between 0 and 100.");
            
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", "Findings: " + findings.toString() + "\n\nCode Context:\n" + contextPayload);
            
            messages.add(systemMsg);
            messages.add(userMsg);
            
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.3);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            logger.info("Calling LLM at {} with model {}", llmEndpoint, llmModel);
            
            // Adjust endpoint if it's pointing to /api/generate (Ollama native) vs /v1/chat/completions (OpenAI compat)
            String endpoint = llmEndpoint;
            if (endpoint.endsWith("/api/generate")) {
                // If the user provided the ollama native endpoint by mistake in config, we can try to adapt or just let it fail to fallback.
                // It's better to just use the endpoint as provided.
            }
            
            Map response = restTemplate.postForObject(endpoint, request, Map.class);
            
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String content = (String) message.get("content");
                    
                    double riskScore = calculateBaseRiskScore(findings); // LLM might not output just a number, so we combine logic or parse. We'll use a calculated base score and just use LLM for explanation.
                    
                    return new LlmResult(content, riskScore, "llm");
                }
            } else if (response != null && response.containsKey("response")) {
                // Ollama /api/generate fallback handling in case they use the native endpoint
                String content = (String) response.get("response");
                double riskScore = calculateBaseRiskScore(findings);
                return new LlmResult(content, riskScore, "llm");
            }
            
            logger.warn("Unrecognized LLM response format, falling back.");
            throw new RuntimeException("Unrecognized LLM response");
            
        } catch (Exception e) {
            logger.error("LLM call failed: {}. Triggering fallback.", e.getMessage());
            return fallbackExplainer.explain(contextPayload, findings);
        }
    }
    
    private double calculateBaseRiskScore(JsonNode findings) {
        double score = 0;
        if (findings != null && findings.isArray()) {
            for (JsonNode finding : findings) {
                String severity = finding.path("severity").asText("LOW");
                if ("CRITICAL".equalsIgnoreCase(severity)) score += 30;
                else if ("HIGH".equalsIgnoreCase(severity)) score += 20;
                else if ("MEDIUM".equalsIgnoreCase(severity)) score += 10;
                else score += 5;
            }
        }
        return Math.min(score, 100.0);
    }
}
