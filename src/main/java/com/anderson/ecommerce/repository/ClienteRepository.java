package com.anderson.ecommerce.repository;

import com.anderson.ecommerce.model.resource.ClienteModelResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModelResource, String> {
    boolean existsByEmail (String email);
}
