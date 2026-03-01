package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.auth.AuthResponseDTO;
import com.argenischacon.inventory_sales_system.dto.auth.LoginRequestDTO;
import com.argenischacon.inventory_sales_system.dto.auth.RegisterRequestDTO;
import com.argenischacon.inventory_sales_system.enums.Role;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.mapper.UserMapper;
import com.argenischacon.inventory_sales_system.model.User;
import com.argenischacon.inventory_sales_system.repository.UserRepository;
import com.argenischacon.inventory_sales_system.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new RegisterRequestDTO("testuser", "password123", "test@test.com");
        loginRequestDTO = new LoginRequestDTO("testuser", "password123");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .password("encodedPassword")
                .roles(Set.of(Role.USER))
                .build();
    }

    @Test
    void register_Success() {
        when(userRepository.existsByUsername(registerRequestDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequestDTO.email())).thenReturn(false);
        when(userMapper.toEntity(any(RegisterRequestDTO.class), any(PasswordEncoder.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtils.generateToken(any(UserDetails.class))).thenReturn("mockJwtToken");

        AuthResponseDTO response = authService.register(registerRequestDTO);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.token());
        assertEquals("testuser", response.username());
        assertEquals("Bearer", response.tokenType());

        verify(userRepository, times(1)).existsByUsername(registerRequestDTO.username());
        verify(userRepository, times(1)).existsByEmail(registerRequestDTO.email());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtUtils, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    void register_ThrowsException_WhenUsernameExists() {
        when(userRepository.existsByUsername(registerRequestDTO.username())).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> authService.register(registerRequestDTO));

        assertEquals("User already exists with username : 'testuser'", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(registerRequestDTO.username());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_ThrowsException_WhenEmailExists() {
        when(userRepository.existsByUsername(registerRequestDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequestDTO.email())).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> authService.register(registerRequestDTO));

        assertEquals("User already exists with email : 'test@test.com'", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(registerRequestDTO.username());
        verify(userRepository, times(1)).existsByEmail(registerRequestDTO.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "testuser",
                "encodedPassword",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken(any(UserDetails.class))).thenReturn("mockJwtToken");

        AuthResponseDTO response = authService.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals("mockJwtToken", response.token());
        assertEquals("testuser", response.username());
        assertEquals("Bearer", response.tokenType());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateToken(any(UserDetails.class));
    }

    @Test
    void login_ThrowsException_WhenBadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequestDTO));
    }
}
