package com.scaler.project.productservice.services;

import com.scaler.project.productservice.exceptions.ProductNotFoundException;
import com.scaler.project.productservice.models.Product;

import javax.management.InstanceNotFoundException;
import java.util.List;

public interface ProductService {
    Product getProductById(Long id) throws ProductNotFoundException;

    List<Product> getAllProducts();

    Product replaceProduct(Long id, Product product);

    Product updateProduct(Long id, Product product) throws ProductNotFoundException;

    void deleteProductById(Long id);
}
