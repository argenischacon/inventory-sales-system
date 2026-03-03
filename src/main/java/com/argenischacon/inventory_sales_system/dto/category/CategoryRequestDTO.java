package com.argenischacon.inventory_sales_system.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Request payload for creating or updating a category")
public record CategoryRequestDTO(
        @Schema(description = "Name of the category", example = "Electronics")
        @NotBlank(message = "Category name is mandatory")
        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        String name,

        @Schema(description = "Detailed description of the category", example = "All kinds of electronic devices")
        @Size(max = 500, message = "Category description must not exceed 500 characters")
        String description) {
}
