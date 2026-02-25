package com.argenischacon.inventory_sales_system.dto.user;

import com.argenischacon.inventory_sales_system.enums.Role;

import java.util.Set;

import lombok.Builder;

@Builder
public record UserResponseDTO(
        Long id,
        String username,
        String email,
        Set<Role> roles
) {}
