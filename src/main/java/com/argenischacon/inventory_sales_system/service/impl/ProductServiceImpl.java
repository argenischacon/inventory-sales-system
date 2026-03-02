package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.product.ProductRequestDTO;
import com.argenischacon.inventory_sales_system.dto.product.ProductResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.ProductMapper;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.Supplier;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
import com.argenischacon.inventory_sales_system.repository.ProductRepository;
import com.argenischacon.inventory_sales_system.repository.SupplierRepository;
import com.argenischacon.inventory_sales_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        if (productRepository.existsBySku(requestDTO.sku())) {
            throw new ResourceAlreadyExistsException("Product", "sku", requestDTO.sku());
        }

        Category category = getCategoryOrThrow(requestDTO.categoryId());
        Supplier supplier = getSupplierOrThrow(requestDTO.supplierId());

        Product product = productMapper.toEntity(requestDTO);
        product.setCategory(category);
        product.setSupplier(supplier);

        product = productRepository.save(product);

        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = getProductOrThrow(id);
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product product = getProductOrThrow(id);

        if (productRepository.existsBySkuAndIdNot(requestDTO.sku(), id)) {
            throw new ResourceAlreadyExistsException("Product", "sku", requestDTO.sku());
        }

        Category category = getCategoryOrThrow(requestDTO.categoryId());
        Supplier supplier = getSupplierOrThrow(requestDTO.supplierId());

        productMapper.updateEntityFromDto(requestDTO, product);
        product.setCategory(category);
        product.setSupplier(supplier);

        product = productRepository.save(product);

        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductOrThrow(id);

        if (!product.getSaleDetails().isEmpty()) {
            throw new ResourceInUseException("Product", "id", id, "sale details");
        }

        productRepository.delete(product);
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
    }
}
