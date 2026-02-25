package com.argenischacon.inventory_sales_system.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;

@Builder
public record CategoryRequestDTO(
        @NotBlank(message = "Category name is mandatory")
        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        String name,

        @Size(max = 500, message = "Category description must not exceed 500 characters")
        String description
) {}
