package com.argenischacon.inventory_sales_system.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProductRequestDTO(
        @NotBlank(message = "SKU is mandatory")
        @Size(max = 50, message = "SKU must not exceed 50 characters")
        String sku,

        @NotBlank(message = "Product name is mandatory")
        @Size(min = 2, max = 150, message = "Product name must be between 2 and 150 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Unit price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
        BigDecimal unitPrice,

        @NotNull(message = "Stock is mandatory")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock,

        @NotNull(message = "Category ID is mandatory")
        Long categoryId,

        @NotNull(message = "Supplier ID is mandatory")
        Long supplierId
) {}
