package com.argenischacon.inventory_sales_system.integration;

import com.argenischacon.inventory_sales_system.dto.category.CategoryRequestDTO;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
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

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    void createCategory_Success() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("IntegrationTestCategory", "Desc");

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("IntegrationTestCategory"));

        assertEquals(1, (long) categoryRepository.count());
    }

    @Test
    void getAllCategories_Success() throws Exception {
        Category category = new Category();
        category.setName("Category 1");
        category.setCode("CODE-2");
        categoryRepository.save(category);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Category 1"));
    }

    @Test
    void getCategoryById_Success() throws Exception {
        Category category = new Category();
        category.setName("Category 2");
        category.setCode("CODE-3");
        category = categoryRepository.save(category);

        mockMvc.perform(get("/api/v1/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("Category 2"));
    }

    @Test
    void updateCategory_Success() throws Exception {
        Category category = new Category();
        category.setName("Category 3");
        category.setCode("CODE-4");
        category = categoryRepository.save(category);

        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Updated Category 3", "Updated desc");

        mockMvc.perform(put("/api/v1/categories/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category 3"));
    }

    @Test
    void deleteCategory_Success() throws Exception {
        Category category = new Category();
        category.setName("Category 4");
        category.setCode("CODE-5");
        category = categoryRepository.save(category);

        mockMvc.perform(delete("/api/v1/categories/" + category.getId()))
                .andExpect(status().isNoContent());

        assertEquals(0, categoryRepository.count());
    }
}
