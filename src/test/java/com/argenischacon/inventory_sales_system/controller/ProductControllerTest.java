package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.category.CategoryNestedDTO;
import com.argenischacon.inventory_sales_system.dto.product.ProductRequestDTO;
import com.argenischacon.inventory_sales_system.dto.product.ProductResponseDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierNestedDTO;
import com.argenischacon.inventory_sales_system.exception.GlobalExceptionHandler;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProductService productService;

        @Autowired
        private ObjectMapper objectMapper;
        private ProductRequestDTO requestDTO;
        private ProductResponseDTO responseDTO;

        @BeforeEach
        void setUp() {
                requestDTO = ProductRequestDTO.builder()
                                .sku("SKU-123")
                                .name("Test Product")
                                .unitPrice(new BigDecimal("99.99"))
                                .stock(50)
                                .categoryId(1L)
                                .supplierId(2L)
                                .build();

                responseDTO = ProductResponseDTO.builder()
                                .id(1L)
                                .sku("SKU-123")
                                .name("Test Product")
                                .unitPrice(new BigDecimal("99.99"))
                                .stock(50)
                                .category(new CategoryNestedDTO(1L, "Category 1"))
                                .supplier(new SupplierNestedDTO(2L, "TAX-123", "Supplier 1"))
                                .build();
        }

        @Test
        void createProduct_Success() throws Exception {
                when(productService.createProduct(any(ProductRequestDTO.class))).thenReturn(responseDTO);

                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.sku").value("SKU-123"))
                                .andExpect(jsonPath("$.name").value("Test Product"))
                                .andExpect(jsonPath("$.unitPrice").value(99.99))
                                .andExpect(jsonPath("$.stock").value(50))
                                .andExpect(jsonPath("$.category.id").value(1L))
                                .andExpect(jsonPath("$.supplier.id").value(2L));
        }

        @Test
        void getProductById_Success() throws Exception {
                when(productService.getProductById(1L)).thenReturn(responseDTO);

                mockMvc.perform(get("/api/v1/products/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.sku").value("SKU-123"));
        }

        @Test
        void getProductById_NotFound() throws Exception {
                when(productService.getProductById(99L)).thenThrow(new ResourceNotFoundException("Product", "id", 99L));

                mockMvc.perform(get("/api/v1/products/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getAllProducts_Success() throws Exception {
                Page<ProductResponseDTO> page = new PageImpl<>(List.of(responseDTO));
                when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/v1/products?page=0&size=10"))
                                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].sku").value("SKU-123"))
                                .andExpect(jsonPath("$.page.totalElements").value(1));
        }

        @Test
        void updateProduct_Success() throws Exception {
                when(productService.updateProduct(eq(1L), any(ProductRequestDTO.class))).thenReturn(responseDTO);

                mockMvc.perform(put("/api/v1/products/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.sku").value("SKU-123"));
        }

        @Test
        void deleteProduct_Success() throws Exception {
                doNothing().when(productService).deleteProduct(1L);

                mockMvc.perform(delete("/api/v1/products/1"))
                                .andExpect(status().isNoContent());

                verify(productService).deleteProduct(1L);
        }
}
