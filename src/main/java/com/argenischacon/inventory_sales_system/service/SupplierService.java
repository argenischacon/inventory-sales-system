package com.argenischacon.inventory_sales_system.service;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    SupplierResponseDTO createSupplier(SupplierRequestDTO requestDTO);

    SupplierResponseDTO getSupplierById(Long id);

    Page<SupplierResponseDTO> getAllSuppliers(Pageable pageable);

    SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO requestDTO);

    void deleteSupplier(Long id);
}
