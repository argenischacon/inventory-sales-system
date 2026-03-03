package com.argenischacon.inventory_sales_system.dto.customer;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing a full customer record")
public record CustomerResponseDTO(
                @Schema(description = "Unique identifier of the customer", example = "1") Long id,

                @Schema(description = "Customer DNI/ID number", example = "V-12345678") String dni,

                @Schema(description = "Customer first name", example = "John") String name,

                @Schema(description = "Customer last name", example = "Doe") String lastname,

                @Schema(description = "Customer phone number", example = "+584141234567") String phone,

                @Schema(description = "Customer email address", example = "john.doe@example.com") String email,

                @Schema(description = "Timestamp when the record was created", example = "2023-10-15T10:30:00") LocalDateTime createdAt,

                @Schema(description = "Timestamp when the record was last updated", example = "2023-10-16T11:45:00") LocalDateTime updatedAt) {
}
