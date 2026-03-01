package com.argenischacon.inventory_sales_system.mapper;

import com.argenischacon.inventory_sales_system.dto.auth.RegisterRequestDTO;
import com.argenischacon.inventory_sales_system.dto.user.UserResponseDTO;
import com.argenischacon.inventory_sales_system.enums.Role;
import com.argenischacon.inventory_sales_system.model.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Mapper(componentModel = "spring", imports = {Role.class, Set.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(Set.of(Role.USER))")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.password()))")
    User toEntity(RegisterRequestDTO dto, @Context PasswordEncoder passwordEncoder);

    UserResponseDTO toDto(User user);
}
