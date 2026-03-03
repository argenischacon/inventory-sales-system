package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleResponseDTO;
import com.argenischacon.inventory_sales_system.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody @Valid SaleRequestDTO requestDTO) {
        SaleResponseDTO response = saleService.createSale(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable Long id) {
        SaleResponseDTO response = saleService.getSaleById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<SaleResponseDTO>> getAllSales(
            @PageableDefault(size = 10, sort = "date", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<SaleResponseDTO> response = saleService.getAllSales(pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
