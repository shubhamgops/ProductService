package com.scaler.project.productservice.services;

import com.scaler.project.productservice.dtos.FakeStoreProductDTO;
import com.scaler.project.productservice.exceptions.ProductNotFoundException;
import com.scaler.project.productservice.models.Category;
import com.scaler.project.productservice.models.Product;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService {


    RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        FakeStoreProductDTO fakeStoreProductDTO =
                restTemplate.getForObject("https://fakestoreapi.com/products/" + id, FakeStoreProductDTO.class);

        if (fakeStoreProductDTO == null) throw new ProductNotFoundException(100L, "Product Not Found for Id: " + id);

        return convertFakeProductStoreDtoToProduct(fakeStoreProductDTO);
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDTO[] fakeStoreProductDTOS = restTemplate.getForObject("https://fakestoreapi.com/products", FakeStoreProductDTO[].class);
        List<Product> productList = new ArrayList<>();
        for (FakeStoreProductDTO fakeStoreProductDTO: fakeStoreProductDTOS) {
            productList.add(convertFakeProductStoreDtoToProduct(fakeStoreProductDTO));
        }
        return productList;
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = convertProductToFakeStoreProductDTO(product);
        return callUpdateProduct(id, fakeStoreProductDTO);
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductNotFoundException {
        FakeStoreProductDTO fakeStoreProductDTO = convertPatchProductToFakeStoreProductDTO(id, product);
        return  callUpdateProduct(id, fakeStoreProductDTO);
    }

    @Override
    public void deleteProductById(Long id) {
        restTemplate.delete("https://fakestoreapi.com/products/" + id);
    }

    private Product callUpdateProduct(Long id, FakeStoreProductDTO fakeStoreProductDTO) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDTO, FakeStoreProductDTO.class);
        ResponseExtractor<ResponseEntity<FakeStoreProductDTO>> responseExtractor = restTemplate.responseEntityExtractor(FakeStoreProductDTO.class);
        FakeStoreProductDTO fakeStoreProductDTO1 = restTemplate
                .execute("https://fakestoreapi.com/products/" + id, HttpMethod.PUT, requestCallback, responseExtractor)
                .getBody();
        return convertFakeProductStoreDtoToProduct(fakeStoreProductDTO1);
    }

    private FakeStoreProductDTO convertProductToFakeStoreProductDTO(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();
        fakeStoreProductDTO.setId(product.getId());
        fakeStoreProductDTO.setTitle(product.getTitle());
        fakeStoreProductDTO.setDescription(product.getDesc());
        fakeStoreProductDTO.setPrice(product.getPrice());
        fakeStoreProductDTO.setCategory(product.getCategory().getTitle());
        return fakeStoreProductDTO;
    }

    private FakeStoreProductDTO convertPatchProductToFakeStoreProductDTO(Long id, Product product) throws ProductNotFoundException {

        Product product1 = getProductById(id);

        if (product.getTitle() != null) {
            product1.setTitle(product.getTitle());
        }

        if (product.getDesc() != null) {
            product1.setDesc(product.getDesc());
        }

        if (product.getPrice() != null) {
            product1.setPrice(product.getPrice());
        }

        if (product.getCategory() != null && product.getCategory().getTitle() != null) {
            product1.getCategory().setTitle(product.getCategory().getTitle());
        }
        return convertProductToFakeStoreProductDTO(product1);
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
