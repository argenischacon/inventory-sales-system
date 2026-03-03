package com.argenischacon.inventory_sales_system.dto.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for adding a product to a sale")
public record SaleDetailRequestDTO(
        @Schema(description = "ID of the product being sold", example = "1")
        @NotNull(message = "Product ID is mandatory")
        Long productId,

        @Schema(description = "Quantity of the product", example = "2")
        @NotNull(message = "Quantity is mandatory")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity) {
}
