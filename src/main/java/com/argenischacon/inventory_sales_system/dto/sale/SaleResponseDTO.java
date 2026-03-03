package com.argenischacon.inventory_sales_system.dto.sale;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerNestedDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing a full sale transaction")
public record SaleResponseDTO(
                @Schema(description = "Unique identifier of the sale", example = "1") Long id,

                @Schema(description = "Timestamp when the sale occurred", example = "2023-10-15T10:30:00") LocalDateTime date,

                @Schema(description = "Auto-generated ticket or receipt number", example = "TKT-123456789") String ticketNumber,

                @Schema(description = "Total amount of the sale", example = "1999.98") BigDecimal total,

                @Schema(description = "Customer details") CustomerNestedDTO customer,

                @Schema(description = "List of products sold in this transaction") List<SaleDetailResponseDTO> saleDetails) {
}
