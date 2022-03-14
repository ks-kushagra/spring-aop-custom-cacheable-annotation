package com.airtel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airtel.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
    Product findById(int id);
}
