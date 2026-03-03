package com.argenischacon.inventory_sales_system.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for creating or updating a customer")
public record CustomerRequestDTO(
                @Schema(description = "Customer DNI/ID number", example = "V-12345678") @NotBlank(message = "DNI is mandatory") @Size(max = 20, message = "DNI must not exceed 20 characters") String dni,

                @Schema(description = "Customer first name", example = "John") @NotBlank(message = "Name is mandatory") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") String name,

                @Schema(description = "Customer last name", example = "Doe") @Size(max = 100, message = "Lastname must not exceed 100 characters") String lastname,

                @Schema(description = "Customer phone number", example = "+584141234567") @Size(max = 20, message = "Phone must not exceed 20 characters") String phone,

                @Schema(description = "Customer email address", example = "john.doe@example.com") @Email(message = "Email should be valid") @Size(max = 100, message = "Email must not exceed 100 characters") String email) {
}
