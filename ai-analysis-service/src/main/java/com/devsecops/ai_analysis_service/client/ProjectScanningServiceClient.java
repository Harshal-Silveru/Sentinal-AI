package com.devsecops.ai_analysis_service.client;

import com.devsecops.ai_analysis_service.dto.ScanResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProjectScanningServiceClient {

    private final RestTemplate restTemplate;

    public ProjectScanningServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public ScanResponse getScanById(Long scanId) {

        String url = "http://localhost:8082/api/scans/" + scanId;

        try {
            return restTemplate.getForObject(
                    url,
                    ScanResponse.class
            );
        } catch (RestClientException e) {
            throw new RuntimeException(
                    "Unable to verify scan with Project Service"
            );
        }
    }
}
