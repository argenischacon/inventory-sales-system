package com.argenischacon.inventory_sales_system.repository;

import com.argenischacon.inventory_sales_system.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    Optional<Customer> findByDni(String dni);
}
