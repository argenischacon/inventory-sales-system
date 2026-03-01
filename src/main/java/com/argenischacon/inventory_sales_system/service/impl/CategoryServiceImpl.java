package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.category.CategoryRequestDTO;
import com.argenischacon.inventory_sales_system.dto.category.CategoryResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.CategoryMapper;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
import com.argenischacon.inventory_sales_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        Category category = categoryMapper.toEntity(requestDTO);

        String code;
        do {
            code = UUID.randomUUID().toString();
        } while (categoryRepository.existsByCode(code));

        category.setCode(code);
        category = categoryRepository.save(category);

        return categoryMapper.toResponseDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = getCategoryOrThrow(id);

        categoryMapper.updateEntityFromDto(requestDTO, category);
        category = categoryRepository.save(category);

        return categoryMapper.toResponseDTO(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryOrThrow(id);

        if (!category.getProducts().isEmpty()) {
            throw new ResourceInUseException("Category", "id", id, "products");
        }

        categoryRepository.delete(category);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
