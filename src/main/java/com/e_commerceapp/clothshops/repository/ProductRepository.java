package com.e_commerceapp.clothshops.repository;

import com.e_commerceapp.clothshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//@RepositoryRestResource(path = "products")
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryName(String categoryName);

    List<Product> findByBrand(String brandName);

    List<Product> findByCategoryNameAndBrand(String categoryName, String brandName);

    List<Product> findByName(String productName);

    List<Product> findByBrandAndName(String productName, String brandName);

    List<Product> findByCategoryNameAndName(String categoryName, String productName);

    Long countByBrandAndName(String productName, String brandName);

    Long countByCategoryNameAndName(String categoryName, String productName);
}
