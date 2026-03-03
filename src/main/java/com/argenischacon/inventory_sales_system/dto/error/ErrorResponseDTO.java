package com.argenischacon.inventory_sales_system.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standardized error response payload")
public record ErrorResponseDTO(
                @Schema(description = "Timestamp when the error occurred", example = "2023-10-15T10:30:00") LocalDateTime timestamp,

                @Schema(description = "HTTP status code", example = "400") int status,

                @Schema(description = "Short error description", example = "Bad Request") String error,

                @Schema(description = "Detailed error message", example = "Validation failed for object='categoryRequestDTO'. Error count: 1") String message,

                @Schema(description = "Path where the error occurred", example = "/api/v1/categories") String path,

                @Schema(description = "Map of specific validation errors if applicable") Map<String, String> validationErrors) {
}
