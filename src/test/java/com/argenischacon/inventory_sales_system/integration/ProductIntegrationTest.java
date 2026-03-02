package com.argenischacon.inventory_sales_system.integration;

import com.argenischacon.inventory_sales_system.dto.product.ProductRequestDTO;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.Supplier;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
import com.argenischacon.inventory_sales_system.repository.ProductRepository;
import com.argenischacon.inventory_sales_system.repository.SupplierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false) // Ignore security for pure controller tests over DB
@ActiveProfiles("test")
@Transactional
class ProductIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Long categoryId;
        private Long supplierId;

        @BeforeEach
        void setUp() {
                productRepository.deleteAll();
                categoryRepository.deleteAll();
                supplierRepository.deleteAll();

                Category category = Category.builder().name("CategoryTest").code("CAT-001").build();
                category = categoryRepository.save(category);
                categoryId = category.getId();

                Supplier supplier = Supplier.builder().name("SupplierTest").taxId("TAX-001").email("sup@test.com")
                                .build();
                supplier = supplierRepository.save(supplier);
                supplierId = supplier.getId();
        }

        @Test
        void createProduct_Success() throws Exception {
                ProductRequestDTO requestDTO = ProductRequestDTO.builder()
                                .sku("SKU-INT-1")
                                .name("Integration Product")
                                .unitPrice(new BigDecimal("150.00"))
                                .stock(20)
                                .categoryId(categoryId)
                                .supplierId(supplierId)
                                .build();

                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.sku").value("SKU-INT-1"))
                                .andExpect(jsonPath("$.name").value("Integration Product"))
                                .andExpect(jsonPath("$.unitPrice").value(150.00))
                                .andExpect(jsonPath("$.category.id").value(categoryId))
                                .andExpect(jsonPath("$.supplier.id").value(supplierId));

                assertEquals(1, productRepository.count());
        }

        @Test
        void createProduct_ConflictAlreadyExists_BySku() throws Exception {
                ProductRequestDTO requestDTO1 = ProductRequestDTO.builder()
                                .sku("DUPLICATE-SKU")
                                .name("Product 1")
                                .unitPrice(new BigDecimal("10.00"))
                                .stock(5)
                                .categoryId(categoryId)
                                .supplierId(supplierId)
                                .build();
                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO1)))
                                .andExpect(status().isCreated());

                ProductRequestDTO requestDTO2 = ProductRequestDTO.builder()
                                .sku("DUPLICATE-SKU")
                                .name("Product 2")
                                .unitPrice(new BigDecimal("20.00"))
                                .stock(10)
                                .categoryId(categoryId)
                                .supplierId(supplierId)
                                .build();

                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO2)))
                                .andExpect(status().isConflict());

                assertEquals(1, productRepository.count());
        }

        @Test
        void createProduct_SucceedsWithDuplicateNames() throws Exception {
                // Name constraint is NOT unique. As per our analysis
                ProductRequestDTO requestDTO1 = ProductRequestDTO.builder()
                                .sku("SKU-A")
                                .name("Same Name Product")
                                .unitPrice(new BigDecimal("10.00"))
                                .stock(5)
                                .categoryId(categoryId)
                                .supplierId(supplierId)
                                .build();
                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO1)))
                                .andExpect(status().isCreated());

                ProductRequestDTO requestDTO2 = ProductRequestDTO.builder()
                                .sku("SKU-B")
                                .name("Same Name Product")
                                .unitPrice(new BigDecimal("20.00"))
                                .stock(10)
                                .categoryId(categoryId)
                                .supplierId(supplierId)
                                .build();

                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO2)))
                                .andExpect(status().isCreated());

                assertEquals(2, productRepository.count());
        }

        @Test
        void getProductById_NotFound() throws Exception {
                mockMvc.perform(get("/api/v1/products/999"))
                                .andExpect(status().isNotFound());
        }
}
