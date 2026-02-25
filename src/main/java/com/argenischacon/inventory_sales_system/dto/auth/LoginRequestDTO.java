package com.argenischacon.inventory_sales_system.dto.auth;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;

@Builder
public record LoginRequestDTO(
        @NotBlank(message = "Username is mandatory")
        String username,

        @NotBlank(message = "Password is mandatory")
        String password
) {}
