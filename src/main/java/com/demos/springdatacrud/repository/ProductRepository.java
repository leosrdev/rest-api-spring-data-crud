package com.demos.springdatacrud.repository;

import com.demos.springdatacrud.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
