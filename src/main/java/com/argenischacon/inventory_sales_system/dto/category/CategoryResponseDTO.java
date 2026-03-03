package com.argenischacon.inventory_sales_system.dto.category;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing a full category")
public record CategoryResponseDTO(
        @Schema(description = "Unique identifier of the category", example = "1")
        Long id,

        @Schema(description = "Name of the category", example = "Electronics")
        String name,

        @Schema(description = "Detailed description of the category", example = "All kinds of electronic devices")
        String description,

        @Schema(description = "Timestamp when the category was created", example = "2023-10-15T10:30:00")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the category was last updated", example = "2023-10-16T11:45:00")
        LocalDateTime updatedAt) {
}
