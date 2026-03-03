package com.argenischacon.inventory_sales_system.service;

import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaleService {
    SaleResponseDTO createSale(SaleRequestDTO requestDTO);

    SaleResponseDTO getSaleById(Long id);

    Page<SaleResponseDTO> getAllSales(Pageable pageable);

    void deleteSale(Long id);
}
