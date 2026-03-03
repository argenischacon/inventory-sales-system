package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.sale.SaleDetailRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.service.SaleService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaleService saleService;

    @Autowired
    private ObjectMapper objectMapper;

    private SaleRequestDTO requestDTO;
    private SaleResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        SaleDetailRequestDTO detailDTO = SaleDetailRequestDTO.builder()
                .productId(1L)
                .quantity(2)
                .build();

        requestDTO = SaleRequestDTO.builder()
                .customerId(1L)
                .saleDetails(List.of(detailDTO))
                .build();

        responseDTO = SaleResponseDTO.builder()
                .id(1L)
                .ticketNumber("TICKET123456")
                .total(new BigDecimal("2000.00"))
                .build();
    }

    @Test
    void createSale_Success() throws Exception {
        when(saleService.createSale(any(SaleRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.ticketNumber").value("TICKET123456"))
                .andExpect(jsonPath("$.total").value("2000.0"));
    }

    @Test
    void getSaleById_Success() throws Exception {
        when(saleService.getSaleById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/sales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketNumber").value("TICKET123456"));
    }

    @Test
    void getSaleById_NotFound() throws Exception {
        when(saleService.getSaleById(99L)).thenThrow(new ResourceNotFoundException("Sale", "id", 99L));

        mockMvc.perform(get("/api/v1/sales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllSales_Success() throws Exception {
        Page<SaleResponseDTO> page = new PageImpl<>(List.of(responseDTO));
        when(saleService.getAllSales(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/sales?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].ticketNumber").value("TICKET123456"))
                .andExpect(jsonPath("$.page.totalElements").value(1));
    }

    @Test
    void deleteSale_Success() throws Exception {
        doNothing().when(saleService).deleteSale(1L);

        mockMvc.perform(delete("/api/v1/sales/1"))
                .andExpect(status().isNoContent());

        verify(saleService).deleteSale(1L);
    }
}
