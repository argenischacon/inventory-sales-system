package com.argenischacon.inventory_sales_system.dto.product;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Brief product information used in nested responses")
public record ProductNestedDTO(
        @Schema(description = "Unique identifier of the product", example = "1")
        Long id,

        @Schema(description = "Stock Keeping Unit", example = "SKU-1234")
        String sku,

        @Schema(description = "Name of the product", example = "Laptop")
        String name,

        @Schema(description = "Unit price of the product", example = "999.99")
        BigDecimal unitPrice) {
}
