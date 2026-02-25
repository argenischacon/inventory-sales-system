package com.argenischacon.inventory_sales_system.dto.product;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProductNestedDTO(
        Long id,
        String sku,
        String name,
        BigDecimal unitPrice
) {}
