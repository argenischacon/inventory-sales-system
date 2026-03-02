package com.argenischacon.inventory_sales_system.integration;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerRequestDTO;
import com.argenischacon.inventory_sales_system.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void createCustomer_Success() throws Exception {
        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .dni("V-INT-1")
                .name("Integration")
                .lastname("Test")
                .phone("123")
                .email("int@test.com")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dni").value("V-INT-1"))
                .andExpect(jsonPath("$.name").value("Integration"));

        assertEquals(1, customerRepository.count());
    }

    @Test
    void createCustomer_ConflictAlreadyExists_ByDni() throws Exception {
        CustomerRequestDTO requestDTO1 = CustomerRequestDTO.builder()
                .dni("DUPLICATE-DNI")
                .name("Customer 1")
                .build();
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO1)))
                .andExpect(status().isCreated());

        CustomerRequestDTO requestDTO2 = CustomerRequestDTO.builder()
                .dni("DUPLICATE-DNI")
                .name("Customer 2")
                .build();

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO2)))
                .andExpect(status().isConflict());

        assertEquals(1, customerRepository.count());
    }

    @Test
    void getCustomerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/customers/999"))
                .andExpect(status().isNotFound());
    }
}
