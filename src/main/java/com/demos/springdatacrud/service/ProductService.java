package com.demos.springdatacrud.service;

import com.demos.springdatacrud.dto.ProductRequest;
import com.demos.springdatacrud.dto.ProductResponse;
import com.demos.springdatacrud.exception.ProductNotFoundException;
import com.demos.springdatacrud.model.Product;
import com.demos.springdatacrud.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse create(ProductRequest productRequest) {
        Product product = mapToEntity(productRequest);
        Product newProduct = productRepository.save(product);
        return mapToResponse(newProduct);
    }

    public ProductResponse update(Integer id, ProductRequest productRequest) throws ProductNotFoundException {
        Optional<Product> existingProduct = productRepository.findById(id);

        return existingProduct.map(product -> {
            Product productForUpdate = existingProduct.get().toBuilder()
                    .name(productRequest.getName())
                    .price(productRequest.getPrice())
                    .build();

            Product productSaved = productRepository.save(productForUpdate);
            return mapToResponse(productSaved);

        }).orElseThrow(ProductNotFoundException::new);
    }

    public List<ProductResponse> findAll() {
        Iterable<Product> products = productRepository.findAll();
        List<ProductResponse> list = new ArrayList<>();
        products.forEach(product ->
                list.add(mapToResponse(product)
                )
        );
        return list;
    }

    public Optional<ProductResponse> findById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(ProductService::mapToResponse);
    }


    public void deleteById(Integer id) throws ProductNotFoundException {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException();
        }
    }

    private static ProductResponse mapToResponse(Product productSaved) {
        return ProductResponse.builder()
                .id(productSaved.getId())
                .name(productSaved.getName())
                .price(productSaved.getPrice())
                .build();
    }

    private static Product mapToEntity(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
    }
}
