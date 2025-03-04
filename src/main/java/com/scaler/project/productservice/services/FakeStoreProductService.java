package com.scaler.project.productservice.services;

import com.scaler.project.productservice.dtos.FakeStoreProductDTO;
import com.scaler.project.productservice.models.Category;
import com.scaler.project.productservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FakeStoreProductService implements ProductService {


    RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long id) {
        FakeStoreProductDTO fakeStoreProductDTO =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDTO.class);

        return convertFakeProductStoreDtoToProduct(fakeStoreProductDTO);
    }

    private Product convertFakeProductStoreDtoToProduct(FakeStoreProductDTO fakeStoreProductDTO) {

        Product product = new Product();

        product.setId(fakeStoreProductDTO.getId());
        product.setTitle(fakeStoreProductDTO.getTitle());
        product.setDesc(fakeStoreProductDTO.getDescription());
        product.setPrice(fakeStoreProductDTO.getPrice());

        // Convert Category
        Category category = new Category();
        category.setTitle(fakeStoreProductDTO.getCategory());
        product.setCategory(category);

        return product;
    }

}
