package com.argenischacon.inventory_sales_system.integration;

import com.argenischacon.inventory_sales_system.dto.auth.LoginRequestDTO;
import com.argenischacon.inventory_sales_system.dto.auth.RegisterRequestDTO;
import com.argenischacon.inventory_sales_system.enums.Role;
import com.argenischacon.inventory_sales_system.model.User;
import com.argenischacon.inventory_sales_system.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @BeforeEach
        void setUp() {
                userRepository.deleteAll();
        }

        // ── Helpers ───────────────────────────────────────────────────────────────

        private String getTokenForUser(String username, String password, Set<Role> roles) {
                User user = new User();
                user.setUsername(username);
                user.setEmail(username + "@test.com");
                user.setPassword(passwordEncoder.encode(password));
                user.setRoles(roles);
                userRepository.save(user);

                LoginRequestDTO login = new LoginRequestDTO(username, password);
                try {
                        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(login)))
                                        .andReturn();
                        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
                        return json.get("token").asText();
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        // ── Auth tests ────────────────────────────────────────────────────────────

        @Test
        void register_Success() throws Exception {
                RegisterRequestDTO requestDTO = new RegisterRequestDTO("newuser", "password123", "newuser@test.com");

                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.token").exists())
                                .andExpect(jsonPath("$.username").value("newuser"))
                                .andExpect(jsonPath("$.tokenType").value("Bearer"));

                assertEquals(1, userRepository.count());
        }

        @Test
        void register_ConflictWhenUsernameExists() throws Exception {
                RegisterRequestDTO first = new RegisterRequestDTO("existinguser", "password123", "existing@test.com");
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(first)))
                                .andExpect(status().isCreated());

                RegisterRequestDTO duplicate = new RegisterRequestDTO("existinguser", "otherpass",
                                "duplicate@test.com");
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(duplicate)))
                                .andExpect(status().isConflict());
        }

        @Test
        void register_ConflictWhenEmailExists() throws Exception {
                RegisterRequestDTO first = new RegisterRequestDTO("user1", "password123", "duplicateEmail@test.com");
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(first)))
                                .andExpect(status().isCreated());

                RegisterRequestDTO duplicate = new RegisterRequestDTO("user2", "otherpass", "duplicateEmail@test.com");
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(duplicate)))
                                .andExpect(status().isConflict());
        }

        @Test
        void register_ValidationError_WhenUsernameBlank() throws Exception {
                RegisterRequestDTO requestDTO = new RegisterRequestDTO("", "password123", "blank@test.com");

                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void login_Success() throws Exception {
                RegisterRequestDTO register = new RegisterRequestDTO("loginuser", "password123", "loginuser@test.com");
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(register)))
                                .andExpect(status().isCreated());

                LoginRequestDTO loginDTO = new LoginRequestDTO("loginuser", "password123");
                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").exists())
                                .andExpect(jsonPath("$.username").value("loginuser"));
        }

        @Test
        void login_Unauthorized_WhenWrongPassword() throws Exception {
                RegisterRequestDTO register = new RegisterRequestDTO("badpassuser", "password123",
                                "badpassuser@test.com");
                mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(register)))
                                .andExpect(status().isCreated());

                LoginRequestDTO loginDTO = new LoginRequestDTO("badpassuser", "wrongpassword");
                mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void accessProtectedEndpoint_WithoutToken_Returns401() throws Exception {
                mockMvc.perform(post("/api/v1/categories"))
                                .andExpect(status().isUnauthorized());
        }

        // ── Role-based access tests ───────────────────────────────────────────────

        @Test
        void roleUser_CanRead_ButCannotWrite() throws Exception {
                String userToken = getTokenForUser("regularuser", "password123", Set.of(Role.USER));

                // GET permitido para ROLE_USER
                mockMvc.perform(get("/api/v1/categories")
                                .header("Authorization", "Bearer " + userToken))
                                .andExpect(status().isOk());

                // POST retorna 403 para ROLE_USER
                mockMvc.perform(post("/api/v1/categories")
                                .header("Authorization", "Bearer " + userToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Test\"}"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void roleAdmin_HasFullAccess() throws Exception {
                String adminToken = getTokenForUser("adminuser", "password123", Set.of(Role.ADMIN));

                // GET permitido para ROLE_ADMIN
                mockMvc.perform(get("/api/v1/categories")
                                .header("Authorization", "Bearer " + adminToken))
                                .andExpect(status().isOk());

                // POST permitido (falla por validación, no por seguridad → 400, no 403)
                mockMvc.perform(post("/api/v1/categories")
                                .header("Authorization", "Bearer " + adminToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"\"}"))
                                .andExpect(status().isBadRequest());
        }
}
