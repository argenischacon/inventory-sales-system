package com.argenischacon.inventory_sales_system.dto.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

@Builder
public record SaleDetailRequestDTO(
        @NotNull(message = "Product ID is mandatory")
        Long productId,

        @NotNull(message = "Quantity is mandatory")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {}
