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
import org.springdoc.core.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Tag(name = "Sales", description = "Endpoints for managing sales transactions")
@SecurityRequirement(name = "bearerAuth")
public class SaleController {

    private final SaleService saleService;

    @Operation(summary = "Create a sale", description = "Records a new sale transaction.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sale created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or insufficient stock"),
            @ApiResponse(responseCode = "404", description = "Customer or Product not found")
    })
    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody @Valid SaleRequestDTO requestDTO) {
        SaleResponseDTO response = saleService.createSale(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a sale by ID", description = "Retrieves details of a sale based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sale retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDTO> getSaleById(@PathVariable Long id) {
        SaleResponseDTO response = saleService.getSaleById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all sales", description = "Retrieves a paginated list of all sales.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<Page<SaleResponseDTO>> getAllSales(
            @ParameterObject @PageableDefault(size = 10, sort = "date", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<SaleResponseDTO> response = saleService.getAllSales(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a sale", description = "Deletes a sale based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sale deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sale not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
