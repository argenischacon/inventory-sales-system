package com.argenischacon.inventory_sales_system.integration;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.model.Supplier;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SupplierIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private SupplierRepository supplierRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                supplierRepository.deleteAll();
        }

        @Test
        void createSupplier_Success() throws Exception {
                SupplierRequestDTO requestDTO = SupplierRequestDTO.builder()
                                .taxId("TAX-1")
                                .name("TechCorp")
                                .email("techcorp@email.com")
                                .phone("555-1234")
                                .address("123 Main St")
                                .build();

                mockMvc.perform(post("/api/v1/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("TechCorp"))
                                .andExpect(jsonPath("$.email").value("techcorp@email.com"));

                assertEquals(1, (long) supplierRepository.count());
        }

        @Test
        void createSupplier_ConflictAlreadyExists_ByTaxId() throws Exception {
                Supplier supplier = new Supplier();
                supplier.setName("SupplierA");
                supplier.setTaxId("TAX-DUPLICATE");
                supplier.setEmail("supplierA@email.com");
                supplierRepository.save(supplier);

                SupplierRequestDTO requestDTO = SupplierRequestDTO.builder()
                                .taxId("TAX-DUPLICATE")
                                .name("SupplierB")
                                .email("supplierB@email.com")
                                .build();

                mockMvc.perform(post("/api/v1/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isConflict());
        }

        @Test
        void getAllSuppliers_Success() throws Exception {
                Supplier supplier = new Supplier();
                supplier.setName("Supplier 1");
                supplier.setTaxId("TAX-4");
                supplier.setEmail("supplier1@email.com");
                supplierRepository.save(supplier);

                mockMvc.perform(get("/api/v1/suppliers"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].name").value("Supplier 1"));
        }

        @Test
        void getSupplierById_Success() throws Exception {
                Supplier supplier = new Supplier();
                supplier.setName("Supplier 2");
                supplier.setTaxId("TAX-5");
                supplier.setEmail("supplier2@email.com");
                supplier = supplierRepository.save(supplier);

                mockMvc.perform(get("/api/v1/suppliers/" + supplier.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(supplier.getId()))
                                .andExpect(jsonPath("$.name").value("Supplier 2"));
        }

        @Test
        void getSupplierById_NotFound() throws Exception {
                mockMvc.perform(get("/api/v1/suppliers/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void updateSupplier_Success() throws Exception {
                Supplier supplier = new Supplier();
                supplier.setName("Supplier 3");
                supplier.setTaxId("TAX-6");
                supplier.setEmail("supplier3@email.com");
                supplier = supplierRepository.save(supplier);

                SupplierRequestDTO requestDTO = SupplierRequestDTO.builder()
                                .taxId("TAX-6")
                                .name("Updated Supplier 3")
                                .email("updated3@email.com")
                                .phone("555-9999")
                                .address("New Address")
                                .build();

                mockMvc.perform(put("/api/v1/suppliers/" + supplier.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Updated Supplier 3"))
                                .andExpect(jsonPath("$.email").value("updated3@email.com"));
        }

        @Test
        void deleteSupplier_Success() throws Exception {
                Supplier supplier = new Supplier();
                supplier.setName("Supplier 4");
                supplier.setTaxId("TAX-7");
                supplier.setEmail("supplier4@email.com");
                supplier = supplierRepository.save(supplier);

                mockMvc.perform(delete("/api/v1/suppliers/" + supplier.getId()))
                                .andExpect(status().isNoContent());

                assertEquals(0, supplierRepository.count());
        }
}
