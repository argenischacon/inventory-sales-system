package com.argenischacon.inventory_sales_system.dto.supplier;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for creating or updating a supplier")
public record SupplierRequestDTO(
                @Schema(description = "Supplier Tax ID/RUT", example = "J-123456789") @NotBlank(message = "Tax ID is mandatory") @Size(max = 20, message = "Tax ID must not exceed 20 characters") String taxId,

                @Schema(description = "Name of the supplier", example = "Tech Supplies Inc.") @NotBlank(message = "Name is mandatory") @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters") String name,

                @Schema(description = "Supplier email address", example = "contact@techsupplies.com") @NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid") @Size(max = 100, message = "Email must not exceed 100 characters") String email,

                @Schema(description = "Supplier phone number", example = "+584141234567") @Size(max = 20, message = "Phone must not exceed 20 characters") String phone,

                @Schema(description = "Physical address of the supplier", example = "123 Main St, City") @Size(max = 500, message = "Address must not exceed 500 characters") String address) {
}
