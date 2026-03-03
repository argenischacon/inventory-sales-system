package com.argenischacon.inventory_sales_system.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for user registration")
public record RegisterRequestDTO(
        @Schema(description = "Desired username for the new account", example = "john_doe")
        @NotBlank(message = "Username is mandatory")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Schema(description = "Password for the new account", example = "strong_password123")
        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        @Schema(description = "Email address for the new account", example = "john.doe@example.com")
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email) {
}
