package com.springboot.swmlopsbe.domain.product.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {}
