package com.argenischacon.inventory_sales_system.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Brief customer information used in nested responses")
public record CustomerNestedDTO(
        @Schema(description = "Unique identifier of the customer", example = "1")
        Long id,

        @Schema(description = "Customer DNI/ID number", example = "V-12345678")
        String dni,

        @Schema(description = "Customer first name", example = "John")
        String name,

        @Schema(description = "Customer last name", example = "Doe")
        String lastname) {
}
