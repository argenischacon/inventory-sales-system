package com.argenischacon.inventory_sales_system.dto.category;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CategoryResponseDTO(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
