package com.argenischacon.inventory_sales_system.service.impl;

import com.argenischacon.inventory_sales_system.dto.sale.SaleDetailRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleRequestDTO;
import com.argenischacon.inventory_sales_system.dto.sale.SaleResponseDTO;
import com.argenischacon.inventory_sales_system.exception.InsufficientStockException;
import com.argenischacon.inventory_sales_system.exception.ResourceNotFoundException;
import com.argenischacon.inventory_sales_system.mapper.SaleMapper;
import com.argenischacon.inventory_sales_system.model.Customer;
import com.argenischacon.inventory_sales_system.model.Product;
import com.argenischacon.inventory_sales_system.model.Sale;
import com.argenischacon.inventory_sales_system.model.SaleDetail;
import com.argenischacon.inventory_sales_system.repository.CustomerRepository;
import com.argenischacon.inventory_sales_system.repository.ProductRepository;
import com.argenischacon.inventory_sales_system.repository.SaleRepository;
import com.argenischacon.inventory_sales_system.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SaleMapper saleMapper;

    @Override
    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(requestDTO.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", requestDTO.customerId()));

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setDate(LocalDateTime.now());
        sale.setTicketNumber(UUID.randomUUID().toString().substring(0, 18).toUpperCase());

        BigDecimal total = BigDecimal.ZERO;

        for (SaleDetailRequestDTO detailDTO : requestDTO.saleDetails()) {
            Product product = productRepository.findById(detailDTO.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", detailDTO.productId()));

            if (product.getStock() < detailDTO.quantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName() + ". Available: " + product.getStock());
            }

            product.setStock(product.getStock() - detailDTO.quantity());
            productRepository.save(product);

            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setProduct(product);
            saleDetail.setQuantity(detailDTO.quantity());
            saleDetail.setUnitPrice(product.getUnitPrice());

            total = total.add(saleDetail.getSubTotal());
            sale.addSaleDetail(saleDetail);
        }

        sale.setTotal(total);
        Sale savedSale = saleRepository.save(sale);

        return saleMapper.toResponseDTO(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponseDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", id));
        return saleMapper.toResponseDTO(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleResponseDTO> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(saleMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", id));

        // Restore stock when deleting a sale
        for (SaleDetail detail : sale.getSaleDetails()) {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        }

        saleRepository.delete(sale);
    }
}
