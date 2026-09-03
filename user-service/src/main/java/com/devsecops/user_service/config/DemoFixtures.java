package com.devsecops.user_service.config;

import com.devsecops.user_service.entity.User;
import com.devsecops.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoFixtures {

    @Bean
    public CommandLineRunner initDemoUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@devsecops.local").isEmpty()) {
                User admin = new User("Admin", "admin@devsecops.local", passwordEncoder.encode("admin123"), "ADMIN");
                userRepository.save(admin);
            }
        };
    }
}
