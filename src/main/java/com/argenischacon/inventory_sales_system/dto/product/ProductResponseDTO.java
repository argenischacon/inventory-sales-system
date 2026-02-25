package com.argenischacon.inventory_sales_system.dto.product;

import com.argenischacon.inventory_sales_system.dto.category.CategoryNestedDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierNestedDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ProductResponseDTO(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal unitPrice,
        Integer stock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CategoryNestedDTO category,
        SupplierNestedDTO supplier
) {}
