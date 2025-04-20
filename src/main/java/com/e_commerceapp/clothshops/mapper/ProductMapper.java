package com.e_commerceapp.clothshops.mapper;

import com.e_commerceapp.clothshops.model.Category;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.dto.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product createProductFromProductDTO(ProductDTO request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }
    public ProductDTO createProductDTOFromProduct(Product request) {
        return new ProductDTO(
                request.getId(),
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                request.getCategory()
        );
    }
}
