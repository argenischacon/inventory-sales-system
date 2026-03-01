package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.auth.AuthResponseDTO;
import com.argenischacon.inventory_sales_system.dto.auth.LoginRequestDTO;
import com.argenischacon.inventory_sales_system.dto.auth.RegisterRequestDTO;
import com.argenischacon.inventory_sales_system.enums.Role;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.model.User;
import com.argenischacon.inventory_sales_system.repository.UserRepository;
import com.argenischacon.inventory_sales_system.security.JwtUtils;
import com.argenischacon.inventory_sales_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.argenischacon.inventory_sales_system.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.username())) {
            throw new ResourceAlreadyExistsException("User", "username", requestDTO.username());
        }

        if (userRepository.existsByEmail(requestDTO.email())) {
            throw new ResourceAlreadyExistsException("User", "email", requestDTO.email());
        }

        User user = userMapper.toEntity(requestDTO, passwordEncoder);

        userRepository.save(user);

        var authorities = user.getRoles().stream()
                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + role.name()))
                .collect(java.util.stream.Collectors.toList());

        var userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);

        String token = jwtUtils.generateToken(userDetails);
        return new AuthResponseDTO(token, userDetails.getUsername());
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.username(),
                        requestDTO.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        return new AuthResponseDTO(token, userDetails.getUsername());
    }
}
