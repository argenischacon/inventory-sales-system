package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.sale.SaleDetailRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleResponseDTO;
import com.argenischacon.inventory_sales_system.exception.InsufficientStockException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.SaleMapper;
import com.argenischacon.inventory_sales_system.model.Customer;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.Sale;
import com.argenischacon.inventory_sales_system.model.SaleDetail;
import com.argenischacon.inventory_sales_system.repository.CustomerRepository;
import com.argenischacon.inventory_sales_system.repository.ProductRepository;
import com.argenischacon.inventory_sales_system.repository.SaleRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Customer customer;
    private Product product;
    private SaleRequestDTO requestDTO;
    private SaleResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .dni("V-1234")
                .name("John")
                .build();

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .unitPrice(new BigDecimal("1000.00"))
                .stock(10)
                .build();

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
                .ticketNumber("TICKET-123")
                .total(new BigDecimal("2000.00"))
                .build();
    }

    @Test
    void createSale_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale sale = invocation.getArgument(0);
            sale.setId(1L);
            return sale;
        });
        when(saleMapper.toResponseDTO(any(Sale.class))).thenReturn(responseDTO);

        SaleResponseDTO result = saleService.createSale(requestDTO);

        assertNotNull(result);
        assertEquals(new BigDecimal("2000.00"), result.total());

        // Product stock should have been updated by subtracting 2
        assertEquals(8, product.getStock());

        verify(customerRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void createSale_ThrowsExceptionIfCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> saleService.createSale(requestDTO));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void createSale_ThrowsExceptionIfProductNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> saleService.createSale(requestDTO));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void createSale_ThrowsExceptionIfInsufficientStock() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        product.setStock(1); // Set stock lower than requested quantity (2)
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> saleService.createSale(requestDTO));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void getSaleById_Success() {
        Sale sale = new Sale();
        sale.setId(1L);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.toResponseDTO(sale)).thenReturn(responseDTO);

        SaleResponseDTO result = saleService.getSaleById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getSaleById_NotFound() {
        when(saleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> saleService.getSaleById(99L));
    }

    @Test
    void getAllSales_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sale> salePage = new PageImpl<>(List.of(new Sale()));

        when(saleRepository.findAll(pageable)).thenReturn(salePage);
        when(saleMapper.toResponseDTO(any(Sale.class))).thenReturn(responseDTO);

        Page<SaleResponseDTO> result = saleService.getAllSales(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deleteSale_Success() {
        Sale sale = new Sale();
        sale.setId(1L);
        SaleDetail detail = new SaleDetail();
        detail.setProduct(product);
        detail.setQuantity(5);

        List<SaleDetail> details = new ArrayList<>();
        details.add(detail);
        sale.setSaleDetails(details);

        int initialStock = product.getStock();

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        saleService.deleteSale(1L);

        // Verify stock was restored
        assertEquals(initialStock + 5, product.getStock());
        verify(productRepository).save(product);
        verify(saleRepository).delete(sale);
    }
}
