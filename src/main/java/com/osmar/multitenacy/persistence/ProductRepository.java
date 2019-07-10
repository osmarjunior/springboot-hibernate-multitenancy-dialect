package com.osmar.multitenacy.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osmar.multitenacy.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}