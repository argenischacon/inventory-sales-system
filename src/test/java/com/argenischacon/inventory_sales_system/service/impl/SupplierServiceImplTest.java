package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.mapper.SupplierMapper;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.Supplier;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private Supplier supplier;
    private SupplierRequestDTO requestDTO;
    private SupplierResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        supplier = Supplier.builder()
                .id(1L)
                .name("Global Tech")
                .taxId("TAX-999")
                .email("contact@globaltech.com")
                .products(new HashSet<>())
                .build();

        requestDTO = SupplierRequestDTO.builder()
                .name("Global Tech")
                .taxId("TAX-999")
                .email("contact@globaltech.com")
                .build();

        responseDTO = SupplierResponseDTO.builder()
                .id(1L)
                .name("Global Tech")
                .taxId("TAX-999")
                .email("contact@globaltech.com")
                .build();
    }

    @Test
    void createSupplier_Success() {
        when(supplierRepository.existsByTaxId(requestDTO.taxId())).thenReturn(false);
        when(supplierMapper.toEntity(requestDTO)).thenReturn(supplier);
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        SupplierResponseDTO result = supplierService.createSupplier(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.id(), result.id());
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void createSupplier_ThrowsExceptionIfTaxIdExists() {
        when(supplierRepository.existsByTaxId(requestDTO.taxId())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> supplierService.createSupplier(requestDTO));
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    void getSupplierById_Success() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        SupplierResponseDTO result = supplierService.getSupplierById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getAllSuppliers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Supplier> supplierPage = new PageImpl<>(List.of(supplier));

        when(supplierRepository.findAll(pageable)).thenReturn(supplierPage);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        Page<SupplierResponseDTO> result = supplierService.getAllSuppliers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Global Tech", result.getContent().get(0).name());
    }

    @Test
    void updateSupplier_Success() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.existsByTaxIdAndIdNot(requestDTO.taxId(), 1L)).thenReturn(false);
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        when(supplierMapper.toResponseDTO(supplier)).thenReturn(responseDTO);

        SupplierResponseDTO result = supplierService.updateSupplier(1L, requestDTO);

        assertNotNull(result);
        verify(supplierMapper).updateEntityFromDto(requestDTO, supplier);
        verify(supplierRepository).save(supplier);
    }

    @Test
    void deleteSupplier_Success() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(1L);

        verify(supplierRepository).delete(supplier);
    }

    @Test
    void deleteSupplier_ThrowsExceptionIfAssociatedWithProducts() {
        supplier.getProducts().add(new Product());
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        assertThrows(ResourceInUseException.class, () -> supplierService.deleteSupplier(1L));
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }
}
