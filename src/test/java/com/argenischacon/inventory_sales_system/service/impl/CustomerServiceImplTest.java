package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerRequestDTO;
import com.argenischacon.inventory_sales_system.dto.customer.CustomerResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.CustomerMapper;
import com.argenischacon.inventory_sales_system.model.Customer;
import com.argenischacon.inventory_sales_system.model.Sale;
import com.argenischacon.inventory_sales_system.repository.CustomerRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequestDTO requestDTO;
    private CustomerResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .dni("V-12345678")
                .name("John")
                .lastname("Doe")
                .email("john@test.com")
                .phone("123456789")
                .sales(new ArrayList<>())
                .build();

        requestDTO = CustomerRequestDTO.builder()
                .dni("V-12345678")
                .name("John")
                .lastname("Doe")
                .email("john@test.com")
                .phone("123456789")
                .build();

        responseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .dni("V-12345678")
                .name("John")
                .lastname("Doe")
                .email("john@test.com")
                .phone("123456789")
                .build();
    }

    @Test
    void createCustomer_Success() {
        when(customerRepository.existsByDni(requestDTO.dni())).thenReturn(false);
        when(customerMapper.toEntity(requestDTO)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDTO(customer)).thenReturn(responseDTO);

        CustomerResponseDTO result = customerService.createCustomer(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.dni(), result.dni());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_ThrowsExceptionIfDniExists() {
        when(customerRepository.existsByDni(requestDTO.dni())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> customerService.createCustomer(requestDTO));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDTO(customer)).thenReturn(responseDTO);

        CustomerResponseDTO result = customerService.getCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getCustomerById_ThrowsExceptionIfNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void getAllCustomers_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Customer> customerPage = new PageImpl<>(List.of(customer));

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toResponseDTO(customer)).thenReturn(responseDTO);

        Page<CustomerResponseDTO> result = customerService.getAllCustomers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("V-12345678", result.getContent().get(0).dni());
    }

    @Test
    void updateCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDniAndIdNot(requestDTO.dni(), 1L)).thenReturn(false);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toResponseDTO(customer)).thenReturn(responseDTO);

        CustomerResponseDTO result = customerService.updateCustomer(1L, requestDTO);

        assertNotNull(result);
        verify(customerMapper).updateEntityFromDto(requestDTO, customer);
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_ThrowsExceptionIfDniExistsForOtherId() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDniAndIdNot(requestDTO.dni(), 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> customerService.updateCustomer(1L, requestDTO));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_ThrowsExceptionIfAssociatedWithSales() {
        customer.getSales().add(new Sale());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(ResourceInUseException.class, () -> customerService.deleteCustomer(1L));
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}
