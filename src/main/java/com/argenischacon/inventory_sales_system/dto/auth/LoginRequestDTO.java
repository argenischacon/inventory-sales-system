package com.argenischacon.inventory_sales_system.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for user login")
public record LoginRequestDTO(
        @Schema(description = "User's unique username", example = "admin_user")
        @NotBlank(message = "Username is mandatory")
        String username,

        @Schema(description = "User's password", example = "secret_password")
        @NotBlank(message = "Password is mandatory")
        String password) {
}
