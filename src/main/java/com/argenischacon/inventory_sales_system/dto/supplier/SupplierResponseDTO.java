package com.argenischacon.inventory_sales_system.dto.supplier;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SupplierResponseDTO(
        Long id,
        String taxId,
        String name,
        String email,
        String phone,
        String address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
