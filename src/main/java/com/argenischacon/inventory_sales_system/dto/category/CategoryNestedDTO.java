package com.argenischacon.inventory_sales_system.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Brief category information used in nested responses")
public record CategoryNestedDTO(
                @Schema(description = "Unique identifier of the category", example = "1") Long id,

                @Schema(description = "Name of the category", example = "Electronics") String name) {
}
