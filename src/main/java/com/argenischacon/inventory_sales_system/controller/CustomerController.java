package com.argenischacon.inventory_sales_system.controller;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerRequestDTO;
import com.argenischacon.inventory_sales_system.dto.customer.CustomerResponseDTO;
import com.argenischacon.inventory_sales_system.service.CustomerService;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Endpoints for managing customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create a customer", description = "Creates a new customer record.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody @Valid CustomerRequestDTO requestDTO) {
        CustomerResponseDTO response = customerService.createCustomer(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a customer by ID", description = "Retrieves a customer based on their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all customers", description = "Retrieves a paginated list of all customers.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> getAllCustomers(
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<CustomerResponseDTO> response = customerService.getAllCustomers(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a customer", description = "Updates an existing customer by their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody @Valid CustomerRequestDTO requestDTO) {
        CustomerResponseDTO response = customerService.updateCustomer(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a customer", description = "Deletes a customer based on their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
