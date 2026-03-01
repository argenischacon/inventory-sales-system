package com.argenischacon.inventory_sales_system.repository;

import com.argenischacon.inventory_sales_system.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByTaxId(String taxId);

    boolean existsByTaxIdAndIdNot(String taxId, Long id);

    Optional<Supplier> findByName(String name);
}
