package com.e_commerceapp.clothshops.service.product;

import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.dto.ProductDTO;
import com.e_commerceapp.clothshops.requests.ProductRequests;

import java.util.List;

public interface IProductService {
    //crud operation
    Product addProduct(ProductRequests productRequests);

    Product getProductById(Long productId);

    void deleteProductById(Long productId);

    ProductDTO updateProduct(ProductRequests productRequests,Long productId);

    List<ProductDTO> getAllProducts();

    List<ProductDTO> getAllProductsByCategoryName(String categoryName);

    List<ProductDTO> getAllProductsByBrandName(String brandName);

    List<ProductDTO> getAllProductsByCategoryNameAndBrandName(String categoryName,String brandName);

    List<ProductDTO> getProductsByName(String productName);

    List<ProductDTO> getProductsByBrandNameAndProductName(String productName,String brandName);

    List<ProductDTO> getProductsByCategoryNameAndProductName(String productName,String categoryName);

    Long countProductByBrandNameAndProductName(String productName,String brandName);

    Long countProductByCategoryNameAndProductName(String productName,String categoryName);



}
