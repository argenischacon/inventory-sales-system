package com.argenischacon.inventory_sales_system.dto.sale;

import com.argenischacon.inventory_sales_system.dto.product.ProductNestedDTO;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record SaleDetailResponseDTO(
        Long id,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal,
        ProductNestedDTO product
) {}
