package com.argenischacon.inventory_sales_system.dto.product;

import com.argenischacon.inventory_sales_system.dto.category.CategoryNestedDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierNestedDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing a full product record")
public record ProductResponseDTO(
        @Schema(description = "Unique identifier of the product", example = "1")
        Long id,

        @Schema(description = "Stock Keeping Unit", example = "SKU-1234")
        String sku,

        @Schema(description = "Name of the product", example = "Laptop")
        String name,

        @Schema(description = "Detailed description", example = "High-performance laptop")
        String description,

        @Schema(description = "Unit price of the product", example = "999.99")
        BigDecimal unitPrice,

        @Schema(description = "Available stock quantity", example = "50")
        Integer stock,

        @Schema(description = "Timestamp when the record was created", example = "2023-10-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the record was last updated", example = "2023-10-16T11:45:00")
        LocalDateTime updatedAt,

        @Schema(description = "Category details")
        CategoryNestedDTO category,

        @Schema(description = "Supplier details")
        SupplierNestedDTO supplier) {
}
