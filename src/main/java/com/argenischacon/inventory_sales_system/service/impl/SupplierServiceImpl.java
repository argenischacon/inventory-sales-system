package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.supplier.SupplierRequestDTO;
import com.argenischacon.inventory_sales_system.dto.supplier.SupplierResponseDTO;
import com.argenischacon.inventory_sales_system.exception.ResourceAlreadyExistsException;
import com.argenischacon.inventory_sales_system.exception.ResourceInUseException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.SupplierMapper;
import com.argenischacon.inventory_sales_system.model.Supplier;
import com.argenischacon.inventory_sales_system.repository.SupplierRepository;
import com.argenischacon.inventory_sales_system.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierResponseDTO createSupplier(SupplierRequestDTO requestDTO) {
        if (supplierRepository.existsByTaxId(requestDTO.taxId())) {
            throw new ResourceAlreadyExistsException("Supplier", "taxId", requestDTO.taxId());
        }

        Supplier supplier = supplierMapper.toEntity(requestDTO);
        supplier = supplierRepository.save(supplier);

        return supplierMapper.toResponseDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Long id) {
        Supplier supplier = getSupplierOrThrow(id);
        return supplierMapper.toResponseDTO(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierResponseDTO> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable)
                .map(supplierMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public SupplierResponseDTO updateSupplier(Long id, SupplierRequestDTO requestDTO) {
        Supplier supplier = getSupplierOrThrow(id);

        if (supplierRepository.existsByTaxIdAndIdNot(requestDTO.taxId(), id)) {
            throw new ResourceAlreadyExistsException("Supplier", "taxId", requestDTO.taxId());
        }

        supplierMapper.updateEntityFromDto(requestDTO, supplier);
        supplier = supplierRepository.save(supplier);

        return supplierMapper.toResponseDTO(supplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierOrThrow(id);

        if (!supplier.getProducts().isEmpty()) {
            throw new ResourceInUseException("Supplier", "id", id, "products");
        }

        supplierRepository.delete(supplier);
    }

    private Supplier getSupplierOrThrow(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", id));
    }
}
