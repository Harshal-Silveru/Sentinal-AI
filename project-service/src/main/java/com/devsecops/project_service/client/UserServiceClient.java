package com.devsecops.project_service.client;

import com.devsecops.project_service.dto.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Value("${user-service.url}")
    private String userServiceUrl;

    public UserServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public boolean verifyUserExists(Long userId) {
        String url = userServiceUrl + "/api/users/" + userId;

        try {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            org.springframework.web.context.request.ServletRequestAttributes attrs = 
                    (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (attrs != null && attrs.getRequest().getHeader("Authorization") != null) {
                headers.set("Authorization", attrs.getRequest().getHeader("Authorization"));
            }
            org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
            
            org.springframework.http.ResponseEntity<UserResponse> response = restTemplate.exchange(
                    url, org.springframework.http.HttpMethod.GET, entity, UserResponse.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            throw new RuntimeException("Unable to verify user with User Service");
        }
    }

    public UserResponse getUserById(Long userId) {
        String url = userServiceUrl + "/api/users/" + userId;
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        org.springframework.web.context.request.ServletRequestAttributes attrs = 
                (org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        if (attrs != null && attrs.getRequest().getHeader("Authorization") != null) {
            headers.set("Authorization", attrs.getRequest().getHeader("Authorization"));
        }
        org.springframework.http.HttpEntity<?> entity = new org.springframework.http.HttpEntity<>(headers);
        
        return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, UserResponse.class).getBody();
    }
}
