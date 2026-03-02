package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.product.ProductRequestDTO;
import com.argenischacon.inventory_sales_system.dto.product.ProductResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.ProductMapper;
import com.argenischacon.inventory_sales_system.model.Category;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.SaleDetail;
import com.argenischacon.inventory_sales_system.model.Supplier;
import com.argenischacon.inventory_sales_system.repository.CategoryRepository;
import com.argenischacon.inventory_sales_system.repository.ProductRepository;
import com.argenischacon.inventory_sales_system.repository.SupplierRepository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;
    private Supplier supplier;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Category 1").code("C-1").build();
        supplier = Supplier.builder().id(2L).name("Supplier 1").taxId("T-1").build();

        product = Product.builder()
                .id(1L)
                .sku("SKU-123")
                .name("Product 1")
                .unitPrice(new BigDecimal("10.50"))
                .stock(100)
                .category(category)
                .supplier(supplier)
                .saleDetails(new ArrayList<>())
                .build();

        requestDTO = ProductRequestDTO.builder()
                .sku("SKU-123")
                .name("Product 1")
                .unitPrice(new BigDecimal("10.50"))
                .stock(100)
                .categoryId(1L)
                .supplierId(2L)
                .build();

        responseDTO = ProductResponseDTO.builder()
                .id(1L)
                .sku("SKU-123")
                .name("Product 1")
                .unitPrice(new BigDecimal("10.50"))
                .stock(100)
                .build();
    }

    @Test
    void createProduct_Success() {
        when(productRepository.existsBySku(requestDTO.sku())).thenReturn(false);
        when(categoryRepository.findById(requestDTO.categoryId())).thenReturn(Optional.of(category));
        when(supplierRepository.findById(requestDTO.supplierId())).thenReturn(Optional.of(supplier));
        when(productMapper.toEntity(requestDTO)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.sku(), result.sku());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsExceptionIfSkuExists() {
        when(productRepository.existsBySku(requestDTO.sku())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> productService.createProduct(requestDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void createProduct_ThrowsExceptionIfCategoryNotFound() {
        when(productRepository.existsBySku(requestDTO.sku())).thenReturn(false);
        when(categoryRepository.findById(requestDTO.categoryId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.createProduct(requestDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getProductById_ThrowsExceptionIfNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void getAllProducts_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

        Page<ProductResponseDTO> result = productService.getAllProducts(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("SKU-123", result.getContent().get(0).sku());
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsBySkuAndIdNot(requestDTO.sku(), 1L)).thenReturn(false);
        when(categoryRepository.findById(requestDTO.categoryId())).thenReturn(Optional.of(category));
        when(supplierRepository.findById(requestDTO.supplierId())).thenReturn(Optional.of(supplier));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponseDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.updateProduct(1L, requestDTO);

        assertNotNull(result);
        verify(productMapper).updateEntityFromDto(requestDTO, product);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_ThrowsExceptionIfSkuExistsForOtherId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsBySkuAndIdNot(requestDTO.sku(), 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> productService.updateProduct(1L, requestDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }

    @Test
    void deleteProduct_ThrowsExceptionIfAssociatedWithSaleDetails() {
        product.getSaleDetails().add(new SaleDetail());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ResourceInUseException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).delete(any(Product.class));
    }
}
