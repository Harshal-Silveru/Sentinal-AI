package com.devsecops.project_service.client;

import com.devsecops.project_service.dto.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public UserServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    public UserResponse getUserById(Long userId) {

        String url = "http://localhost:8081/api/users/" + userId;

        return restTemplate.getForObject(
                url,
                UserResponse.class
        );
    }
}
