package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.customer.CustomerRequestDTO;
import com.argenischacon.inventory_sales_system.dto.customer.CustomerResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.CustomerMapper;
import com.argenischacon.inventory_sales_system.model.Customer;
import com.argenischacon.inventory_sales_system.repository.CustomerRepository;
import com.argenischacon.inventory_sales_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        if (customerRepository.existsByDni(requestDTO.dni())) {
            throw new ResourceAlreadyExistsException("Customer", "dni", requestDTO.dni());
        }

        Customer customer = customerMapper.toEntity(requestDTO);
        customer = customerRepository.save(customer);

        return customerMapper.toResponseDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = getCustomerOrThrow(id);
        return customerMapper.toResponseDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer customer = getCustomerOrThrow(id);

        if (customerRepository.existsByDniAndIdNot(requestDTO.dni(), id)) {
            throw new ResourceAlreadyExistsException("Customer", "dni", requestDTO.dni());
        }

        customerMapper.updateEntityFromDto(requestDTO, customer);
        customer = customerRepository.save(customer);

        return customerMapper.toResponseDTO(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerOrThrow(id);

        if (!customer.getSales().isEmpty()) {
            throw new ResourceInUseException("Customer", "id", id, "sales");
        }

        customerRepository.delete(customer);
    }

    private Customer getCustomerOrThrow(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }
}
