package com.argenischacon.inventory_sales_system.config;

import com.argenischacon.inventory_sales_system.enums.Role;
import com.argenischacon.inventory_sales_system.model.User;
import com.argenischacon.inventory_sales_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.user.username}")
    private String adminUsername;

    @Value("${admin.user.email}")
    private String adminEmail;

    @Value("${admin.user.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByRolesContaining(Role.ADMIN)) {
            log.info("No ADMIN user found, creating one...");
            User adminUser = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(Role.ADMIN, Role.USER))
                    .build();
            userRepository.save(adminUser);
            log.info("ADMIN user created successfully.");
        } else {
            log.info("Admin user already exists. Skipping creation.");
        }
    }
}