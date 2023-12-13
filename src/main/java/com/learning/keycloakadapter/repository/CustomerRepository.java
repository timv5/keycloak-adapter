package com.learning.keycloakadapter.repository;

import com.learning.keycloakadapter.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
}
