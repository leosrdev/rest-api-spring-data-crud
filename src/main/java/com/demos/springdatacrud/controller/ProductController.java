package com.demos.springdatacrud.controller;

import com.demos.springdatacrud.dto.ProductRequest;
import com.demos.springdatacrud.dto.ProductResponse;
import com.demos.springdatacrud.exception.ProductNotFoundException;
import com.demos.springdatacrud.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {
        try {
            ProductResponse savedProduct = productService.update(id, productRequest);
            return ResponseEntity.status(HttpStatus.OK).body(savedProduct);
        } catch (ProductNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Integer id) {
        return productService.findById(id)
                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
