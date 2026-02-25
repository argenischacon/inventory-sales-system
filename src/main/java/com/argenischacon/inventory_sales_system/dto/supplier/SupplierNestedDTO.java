package com.argenischacon.inventory_sales_system.dto.supplier;

import lombok.Builder;

@Builder
public record SupplierNestedDTO(
        Long id,
        String taxId,
        String name
) {}
