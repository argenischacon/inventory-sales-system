package com.argenischacon.inventory_sales_system.dto.user;

import com.argenischacon.inventory_sales_system.enums.Role;

import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Response payload representing user details")
public record UserResponseDTO(
                @Schema(description = "Unique identifier of the user", example = "1") Long id,

                @Schema(description = "Username of the user", example = "admin_user") String username,

                @Schema(description = "Email address of the user", example = "admin@example.com") String email,

                @Schema(description = "Set of roles assigned to the user") Set<Role> roles) {
}
