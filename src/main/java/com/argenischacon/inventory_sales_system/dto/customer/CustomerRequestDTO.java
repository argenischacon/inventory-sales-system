package com.argenischacon.inventory_sales_system.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;

@Builder
public record CustomerRequestDTO(
        @NotBlank(message = "DNI is mandatory")
        @Size(max = 20, message = "DNI must not exceed 20 characters")
        String dni,

        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Size(max = 100, message = "Lastname must not exceed 100 characters")
        String lastname,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        @Email(message = "Email should be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email
) {}
