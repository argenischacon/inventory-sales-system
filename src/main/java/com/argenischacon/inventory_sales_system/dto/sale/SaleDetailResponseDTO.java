package com.argenischacon.inventory_sales_system.dto.sale;

import com.argenischacon.inventory_sales_system.dto.product.ProductNestedDTO;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing a line item in a sale")
public record SaleDetailResponseDTO(
        @Schema(description = "Unique identifier of the sale detail", example = "1")
        Long id,

        @Schema(description = "Quantity of the product sold", example = "2")
        Integer quantity,

        @Schema(description = "Unit price at the time of sale", example = "999.99")
        BigDecimal unitPrice,

        @Schema(description = "Subtotal for this line item", example = "1999.98")
        BigDecimal subTotal,

        @Schema(description = "Product details")
        ProductNestedDTO product) {
}
