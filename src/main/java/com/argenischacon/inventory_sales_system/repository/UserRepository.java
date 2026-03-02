package com.argenischacon.inventory_sales_system.repository;

import com.argenischacon.inventory_sales_system.enums.Role;
import com.argenischacon.inventory_sales_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByRolesContaining(Role role);
}
