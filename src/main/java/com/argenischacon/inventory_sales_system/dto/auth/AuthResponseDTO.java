package com.argenischacon.inventory_sales_system.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponseDTO(
        String token,
        String username,
        String tokenType) {

    public AuthResponseDTO(String token, String username) {
        this(token, username, "Bearer");
    }
}
