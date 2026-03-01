package com.argenischacon.inventory_sales_system.service;

import com.argenischacon.inventory_sales_system.dto.auth.AuthResponseDTO;
import com.argenischacon.inventory_sales_system.dto.auth.LoginRequestDTO;
import com.argenischacon.inventory_sales_system.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO requestDTO);

    AuthResponseDTO login(LoginRequestDTO requestDTO);
}
