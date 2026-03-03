package com.argenischacon.inventory_sales_system.dto.supplier;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing a full supplier record")
public record SupplierResponseDTO(
                @Schema(description = "Unique identifier of the supplier", example = "1") Long id,

                @Schema(description = "Supplier Tax ID/RUT", example = "J-123456789") String taxId,

                @Schema(description = "Name of the supplier", example = "Tech Supplies Inc.") String name,

                @Schema(description = "Supplier email address", example = "contact@techsupplies.com") String email,

                @Schema(description = "Supplier phone number", example = "+584141234567") String phone,

                @Schema(description = "Physical address of the supplier", example = "123 Main St, City") String address,

                @Schema(description = "Timestamp when the record was created", example = "2023-10-15T10:30:00") LocalDateTime createdAt,

                @Schema(description = "Timestamp when the record was last updated", example = "2023-10-16T11:45:00") LocalDateTime updatedAt) {
}
