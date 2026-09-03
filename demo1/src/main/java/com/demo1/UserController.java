package com.demo1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @GetMapping("/users")
    public String getUsers() {
        return "List of users";
    }

    private void helper() {
        int x = 0;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) x++;
        }
    }
}
