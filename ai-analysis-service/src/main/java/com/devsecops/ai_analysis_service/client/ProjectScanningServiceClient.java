package com.devsecops.ai_analysis_service.client;

import com.devsecops.ai_analysis_service.dto.ScanResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProjectScanningServiceClient {

    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${project-service.url}")
    private String projectServiceUrl;

    public ProjectScanningServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    private org.springframework.http.HttpEntity<?> createAuthEntity() {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        org.springframework.web.context.request.ServletRequestAttributes attrs = 
                (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getHeader("Authorization") != null) {
            headers.set("Authorization", attrs.getRequest().getHeader("Authorization"));
        }
        return new org.springframework.http.HttpEntity<>(headers);
    }

    public ScanResponse getScanById(Long scanId) {
        String url = projectServiceUrl + "/api/scans/" + scanId;
        try {
            return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, createAuthEntity(), ScanResponse.class).getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Unable to verify scan with Project Service");
        }
    }

    public com.fasterxml.jackson.databind.JsonNode getFindings(Long scanId) {
        String url = projectServiceUrl + "/api/scans/" + scanId + "/findings";
        try {
            String json = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, createAuthEntity(), String.class).getBody();
            return new com.fasterxml.jackson.databind.ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch findings from Project Service", e);
        }
    }

    public com.fasterxml.jackson.databind.JsonNode getSelectedCode(Long scanId) {
        String url = projectServiceUrl + "/api/scans/" + scanId + "/selected-code";
        try {
            String json = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, createAuthEntity(), String.class).getBody();
            return new com.fasterxml.jackson.databind.ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch selected code from Project Service", e);
        }
    }
}
