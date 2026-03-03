package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierResponseDTO;
import com.argenischacon.inventory_sales_system.service.SupplierService;
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
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Endpoints for managing inventory suppliers")
@SecurityRequirement(name = "bearerAuth")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Create a supplier", description = "Creates a new supplier.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDTO> createSupplier(@Valid @RequestBody SupplierRequestDTO requestDTO) {
        return new ResponseEntity<>(supplierService.createSupplier(requestDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a supplier by ID", description = "Retrieves a supplier based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @Operation(summary = "Get all suppliers", description = "Retrieves a paginated list of all suppliers.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<Page<SupplierResponseDTO>> getAllSuppliers(
            @ParameterObject @PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(pageable));
    }

    @Operation(summary = "Update a supplier", description = "Updates an existing supplier by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequestDTO requestDTO) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, requestDTO));
    }

    @Operation(summary = "Delete a supplier", description = "Deletes a supplier based on its unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
