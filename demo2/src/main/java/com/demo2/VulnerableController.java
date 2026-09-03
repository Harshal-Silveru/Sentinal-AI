package com.demo2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.Statement;

@RestController
public class VulnerableController {
    
    private final String SUPER_SECRET = "my_hardcoded_aws_key_12345";
    
    @GetMapping("/vulnerable")
    public String doDangerousThings(String userInput) throws Exception {
        // Rule 1: SQL Injection
        String query = "SELECT * FROM users WHERE name = '" + userInput + "'";
        
        // Rule 2: Dangerous Exec
        Runtime.getRuntime().exec(userInput);
        
        // Cyclomatic complexity
        if (userInput != null) {
            if (userInput.length() > 5) {
                while(true) {
                    break;
                }
            } else if (userInput.equals("admin")) {
                return "Admin";
            }
        }
        
        return "Done";
    }
}
