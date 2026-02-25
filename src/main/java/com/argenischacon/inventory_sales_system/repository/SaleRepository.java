package com.argenischacon.inventory_sales_system.repository;

import com.argenischacon.inventory_sales_system.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findByTicketNumber(String ticketNumber);

    boolean existsByTicketNumber(String ticketNumber);

    List<Sale> findByCustomerId(Long customerId);

    List<Sale> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
