package com.argenischacon.inventory_sales_system.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Brief supplier information used in nested responses")
public record SupplierNestedDTO(
                @Schema(description = "Unique identifier of the supplier", example = "1") Long id,

                @Schema(description = "Supplier Tax ID/RUT", example = "J-123456789") String taxId,

                @Schema(description = "Name of the supplier", example = "Tech Supplies Inc.") String name) {
}
