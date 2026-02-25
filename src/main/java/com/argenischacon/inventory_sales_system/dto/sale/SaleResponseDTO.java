package com.argenischacon.inventory_sales_system.dto.sale;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerNestedDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record SaleResponseDTO(
        Long id,
        LocalDateTime date,
        String ticketNumber,
        BigDecimal total,
        CustomerNestedDTO customer,
        List<SaleDetailResponseDTO> saleDetails
) {}
