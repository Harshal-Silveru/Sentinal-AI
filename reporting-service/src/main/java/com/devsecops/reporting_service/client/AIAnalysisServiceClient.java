package com.devsecops.reporting_service.client;


import com.devsecops.reporting_service.dto.AnalysisResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class AIAnalysisServiceClient {

    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${ai-analysis-service.url}")
    private String aiAnalysisServiceUrl;

    public AIAnalysisServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public AnalysisResponse getAnalysisById(Long analysisId) {
        String url = aiAnalysisServiceUrl + "/api/analyses/" + analysisId;

        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            org.springframework.web.context.request.ServletRequestAttributes attrs = 
                    (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (attrs != null && attrs.getRequest().getHeader("Authorization") != null) {
                headers.set("Authorization", attrs.getRequest().getHeader("Authorization"));
            }
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            
            return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, AnalysisResponse.class).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Unable to verify analysis with AI Analysis Service");
        }
    }
}
