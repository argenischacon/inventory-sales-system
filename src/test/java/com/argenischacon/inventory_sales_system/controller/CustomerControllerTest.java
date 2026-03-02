package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerRequestDTO;
import com.argenischacon.inventory_sales_system.dto.customer.CustomerResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;
    private CustomerRequestDTO requestDTO;
    private CustomerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = CustomerRequestDTO.builder()
                .dni("V-12345678")
                .name("John")
                .lastname("Doe")
                .phone("123456789")
                .email("john@test.com")
                .build();

        responseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .dni("V-12345678")
                .name("John")
                .lastname("Doe")
                .phone("123456789")
                .email("john@test.com")
                .build();
    }

    @Test
    void createCustomer_Success() throws Exception {
        when(customerService.createCustomer(any(CustomerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.dni").value("V-12345678"))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void getCustomerById_Success() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("V-12345678"));
    }

    @Test
    void getCustomerById_NotFound() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new ResourceNotFoundException("Customer", "id", 99L));

        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCustomers_Success() throws Exception {
        Page<CustomerResponseDTO> page = new PageImpl<>(List.of(responseDTO));
        when(customerService.getAllCustomers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/customers?page=0&size=10"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].dni").value("V-12345678"))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    void updateCustomer_Success() throws Exception {
        when(customerService.updateCustomer(eq(1L), any(CustomerRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("V-12345678"));
    }

    @Test
    void deleteCustomer_Success() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1L);
    }
}
