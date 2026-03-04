package com.argenischacon.inventory_sales_system.dto.sale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for creating a new sale transaction")
public record SaleRequestDTO(
        @Schema(description = "ID of the customer making the purchase", example = "1")
        @NotNull(message = "Customer ID is mandatory")
        Long customerId,

        @Schema(description = "List of products being purchased")
        @NotEmpty(message = "Sale details cannot be empty")
        @Valid
        List<SaleDetailRequestDTO> saleDetails) {
}
