package com.trslucas.desafio_anota_ai.services;

import com.trslucas.desafio_anota_ai.domain.category.Category;
import com.trslucas.desafio_anota_ai.domain.category.exceptions.CategoryNotFoundException;
import com.trslucas.desafio_anota_ai.domain.product.Product;
import com.trslucas.desafio_anota_ai.domain.product.ProductDTO;
import com.trslucas.desafio_anota_ai.domain.product.exceptions.ProductNotFoundException;
import com.trslucas.desafio_anota_ai.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    public Product insert(ProductDTO productData) {



        Category productCategory = this.categoryService.getById(productData.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Product newProduct = new Product(productData);
        newProduct.setCategory(productCategory);
        this.repository.save(newProduct);
        return newProduct;
    }

    public List<Product> getAll() {
        return this.repository.findAll();
    }


    public Product update(String productId, ProductDTO productData) {
        Product product = this.repository.findById(productId).orElseThrow(ProductNotFoundException::new);

        this.categoryService.getById(productData.categoryId())
                .ifPresent(product::setCategory);

        if(!productData.title().isEmpty()) product.setTitle(productData.title());
        if(!productData.description().isEmpty()) product.setDescription(productData.description());
        if(!(productData.price() == null)) product.setPrice(productData.price());


        this.repository.save(product);

        return product;

    }


    public void delete(String productId) {
        Product product = this.repository.findById(productId).orElseThrow(ProductNotFoundException::new);
        this.repository.delete(product);

    }
}
