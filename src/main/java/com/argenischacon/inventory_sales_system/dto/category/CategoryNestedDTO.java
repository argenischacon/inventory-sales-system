package com.argenischacon.inventory_sales_system.dto.category;

import lombok.Builder;

@Builder
public record CategoryNestedDTO(
        Long id,
        String name
) {}
