package com.argenischacon.inventory_sales_system.dto.customer;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CustomerResponseDTO(
        Long id,
        String dni,
        String name,
        String lastname,
        String phone,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
