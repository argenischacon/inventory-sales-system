package com.argenischacon.inventory_sales_system.dto.sale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import lombok.Builder;

@Builder
public record SaleRequestDTO(
        @NotNull(message = "Customer ID is mandatory")
        Long customerId,

        @NotEmpty(message = "Sale details cannot be empty")
        @Valid
        List<SaleDetailRequestDTO> saleDetails
) {}
