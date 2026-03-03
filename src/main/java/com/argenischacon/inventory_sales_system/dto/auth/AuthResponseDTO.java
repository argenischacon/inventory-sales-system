package com.argenischacon.inventory_sales_system.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload after successful authentication")
public record AuthResponseDTO(
        @Schema(description = "JWT token for accessing secured endpoints", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String token,

        @Schema(description = "Username of the authenticated user", example = "john_doe") String username,

        @Schema(description = "Type of the authentication token", example = "Bearer") String tokenType) {

    public AuthResponseDTO(String token, String username) {
        this(token, username, "Bearer");
    }
}
