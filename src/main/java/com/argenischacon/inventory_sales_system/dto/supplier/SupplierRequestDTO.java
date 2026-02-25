package com.argenischacon.inventory_sales_system.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;

@Builder
public record SupplierRequestDTO(
        @NotBlank(message = "Tax ID is mandatory")
        @Size(max = 20, message = "Tax ID must not exceed 20 characters")
        String taxId,

        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 150, message = "Name must be between 2 and 150 characters") String name,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address
) {}
