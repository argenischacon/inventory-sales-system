package com.argenischacon.inventory_sales_system.dto.customer;

import lombok.Builder;

@Builder
public record CustomerNestedDTO(
        Long id,
        String dni,
        String name,
        String lastname
) {}
