package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.category.CategoryRequestDTO;
import com.argenischacon.inventory_sales_system.dto.category.CategoryResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.mapper.CategoryMapper;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDTO requestDTO;
    private CategoryResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Electronics")
                .code("CODE-123")
                .description("Electronic Items")
                .products(new HashSet<>())
                .build();

        requestDTO = CategoryRequestDTO.builder()
                .name("Electronics")
                .description("Electronic Items")
                .build();

        responseDTO = CategoryResponseDTO.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic Items")
                .build();
    }

    @Test
    void createCategory_Success() {
        when(categoryMapper.toEntity(requestDTO)).thenReturn(category);
        when(categoryRepository.existsByCode(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.id(), result.id());
        assertEquals(responseDTO.name(), result.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getAllCategories_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

        Page<CategoryResponseDTO> result = categoryService.getAllCategories(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Electronics", result.getContent().get(0).name());
    }

    @Test
    void updateCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.updateCategory(1L, requestDTO);

        assertNotNull(result);
        verify(categoryMapper).updateEntityFromDto(requestDTO, category);
        verify(categoryRepository).save(category);
    }

    @Test
    void deleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).delete(category);
    }

    @Test
    void deleteCategory_ThrowsExceptionIfAssociatedWithProducts() {
        category.getProducts().add(new Product()); // Associate a product
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        assertThrows(ResourceInUseException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}
