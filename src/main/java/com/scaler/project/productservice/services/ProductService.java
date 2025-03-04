package com.scaler.project.productservice.services;

import com.scaler.project.productservice.models.Product;

public interface ProductService {
    Product getProductById(Long id);
}
