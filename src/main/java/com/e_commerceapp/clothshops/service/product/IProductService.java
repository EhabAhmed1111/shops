package com.e_commerceapp.clothshops.service.product;

import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.dto.ProductDTO;

import java.util.List;

public interface IProductService {
    //crud operation
    Product addProduct(ProductDTO productDTO);

    Product getProductById(Long productId);

    void deleteProductById(Long productId);

    Product updateProduct(ProductDTO productDTO,Long productId);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCategoryName(String categoryName);

    List<Product> getAllProductsByBrandName(String brandName);

    List<Product> getAllProductsByCategoryNameAndBrandName(String categoryName,String brandName);

    List<Product> getProductsByName(String productName);

    List<Product> getProductsByBrandNameAndProductName(String productName,String brandName);

    List<Product> getProductsByCategoryNameAndProductName(String productName,String categoryName);

    Long countProductByBrandNameAndProductName(String productName,String brandName);

    Long countProductByCategoryNameAndProductName(String productName,String categoryName);



}
