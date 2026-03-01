package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierResponseDTO;
import com.argenischacon.inventory_sales_system.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SupplierService supplierService;

    private SupplierRequestDTO requestDTO;
    private SupplierResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = SupplierRequestDTO.builder()
                .name("Global Tech")
                .taxId("TAX-999")
                .email("contact@globaltech.com")
                .build();

        responseDTO = SupplierResponseDTO.builder()
                .id(1L)
                .name("Global Tech")
                .taxId("TAX-999")
                .email("contact@globaltech.com")
                .build();
    }

    @Test
    void createSupplier_Success() throws Exception {
        when(supplierService.createSupplier(any(SupplierRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Global Tech"));
    }

    @Test
    void getSupplierById_Success() throws Exception {
        when(supplierService.getSupplierById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Global Tech"));
    }

    @Test
    void getAllSuppliers_Success() throws Exception {
        Page<SupplierResponseDTO> page = new PageImpl<>(List.of(responseDTO));
        when(supplierService.getAllSuppliers(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/suppliers")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Global Tech"));
    }

    @Test
    void updateSupplier_Success() throws Exception {
        when(supplierService.updateSupplier(eq(1L), any(SupplierRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/suppliers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Global Tech"));
    }

    @Test
    void deleteSupplier_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/suppliers/1"))
                .andExpect(status().isNoContent());
    }
}
