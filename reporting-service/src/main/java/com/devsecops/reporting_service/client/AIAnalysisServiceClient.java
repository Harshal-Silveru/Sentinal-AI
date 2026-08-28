package com.devsecops.reporting_service.client;


import com.devsecops.reporting_service.dto.AnalysisResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class AIAnalysisServiceClient {

    private final RestTemplate restTemplate;

    public AIAnalysisServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public AnalysisResponse getAnalysisById(Long analysisId) {

        String url =
                "http://localhost:8083/api/analyses/"
                        + analysisId;

        try {

            return restTemplate.getForObject(
                    url,
                    AnalysisResponse.class
            );

        } catch (RestClientException e) {

            throw new RuntimeException(
                    "Unable to communicate with AI Analysis Service"
            );
        }
    }
}
