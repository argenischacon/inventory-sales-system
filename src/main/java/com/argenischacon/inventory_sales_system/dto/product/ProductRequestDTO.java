package com.argenischacon.inventory_sales_system.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for creating or updating a product")
public record ProductRequestDTO(
                @Schema(description = "Stock Keeping Unit", example = "SKU-1234") @NotBlank(message = "SKU is mandatory") @Size(max = 50, message = "SKU must not exceed 50 characters") String sku,

                @Schema(description = "Name of the product", example = "Laptop") @NotBlank(message = "Product name is mandatory") @Size(min = 2, max = 150, message = "Product name must be between 2 and 150 characters") String name,

                @Schema(description = "Detailed description", example = "High-performance laptop") @Size(max = 500, message = "Description must not exceed 500 characters") String description,

                @Schema(description = "Unit price of the product", example = "999.99") @NotNull(message = "Unit price is mandatory") @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0") BigDecimal unitPrice,

                @Schema(description = "Initial stock quantity", example = "50") @NotNull(message = "Stock is mandatory") @Min(value = 0, message = "Stock cannot be negative") Integer stock,

                @Schema(description = "ID of the category this product belongs to", example = "1") @NotNull(message = "Category ID is mandatory") Long categoryId,

                @Schema(description = "ID of the supplier", example = "1") @NotNull(message = "Supplier ID is mandatory") Long supplierId) {
}
