package com.argenischacon.inventory_sales_system.integration;

import com.argenischacon.inventory_sales_system.dto.sale.SaleDetailRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.model.Customer;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.Supplier;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
import com.argenischacon.inventory_sales_system.repository.CustomerRepository;
import com.argenischacon.inventory_sales_system.repository.ProductRepository;
import com.argenischacon.inventory_sales_system.repository.SaleRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
class SaleIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private SaleRepository saleRepository;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Customer customer;
        private Product product;

        @BeforeEach
        void setUp() {
                saleRepository.deleteAll();
                productRepository.deleteAll();
                customerRepository.deleteAll();
                categoryRepository.deleteAll();
                supplierRepository.deleteAll();

                customer = Customer.builder()
                                .dni("V-INT-99")
                                .name("Integration Customer")
                                .build();
                customer = customerRepository.save(customer);

                Category category = Category.builder()
                                .code("CAT-001")
                                .name("Test Category")
                                .description("Desc")
                                .build();
                category = categoryRepository.save(category);

                Supplier supplier = Supplier.builder()
                                .taxId("J-123")
                                .name("Test Supplier")
                                .email("supplier@test.com")
                                .build();
                supplier = supplierRepository.save(supplier);

                product = Product.builder()
                                .sku("SKU-INT-99")
                                .name("Integration Product")
                                .unitPrice(new BigDecimal("100.00"))
                                .stock(10)
                                .category(category)
                                .supplier(supplier)
                                .build();
                product = productRepository.save(product);
        }

        @Test
        void createSale_Success_And_Stock_Deducted() throws Exception {
                SaleDetailRequestDTO detailDTO = SaleDetailRequestDTO.builder()
                                .productId(product.getId())
                                .quantity(2)
                                .build();

                SaleRequestDTO requestDTO = SaleRequestDTO.builder()
                                .customerId(customer.getId())
                                .saleDetails(List.of(detailDTO))
                                .build();

                mockMvc.perform(post("/api/v1/sales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.total").value(200.00))
                                .andExpect(jsonPath("$.saleDetails[0].subTotal").value(200.00));

                assertEquals(1, saleRepository.count());

                Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
                assertEquals(8, savedProduct.getStock()); // Deducted 2 from 10
        }

        @Test
        void createSale_InsufficientStock_ThrowsConflict() throws Exception {
                // Request 15 items but stock is 10
                SaleDetailRequestDTO detailDTO = SaleDetailRequestDTO.builder()
                                .productId(product.getId())
                                .quantity(15)
                                .build();

                SaleRequestDTO requestDTO = SaleRequestDTO.builder()
                                .customerId(customer.getId())
                                .saleDetails(List.of(detailDTO))
                                .build();

                mockMvc.perform(post("/api/v1/sales")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isBadRequest()); // Handled by GlobalExceptionHandler

                assertEquals(0, saleRepository.count());

                Product savedProduct = productRepository.findById(product.getId()).orElseThrow();
                assertEquals(10, savedProduct.getStock()); // Stock unharmed
        }
}
