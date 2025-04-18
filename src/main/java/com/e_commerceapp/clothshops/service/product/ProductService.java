package com.e_commerceapp.clothshops.service.product;

import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.ProductMapper;
import com.e_commerceapp.clothshops.model.Category;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.CategoryRepository;
import com.e_commerceapp.clothshops.repository.ProductRepository;
import com.e_commerceapp.clothshops.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//this generates constructor for final or not null fields
//suitable for inject dep
@RequiredArgsConstructor
public class ProductService implements IProductService {



    private final ProductRepository productRepository;


    private final CategoryRepository categoryRepository;


    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Product addProduct(ProductDTO productDTO) {
        //check if category is found in the DB
        //if yes, set it as new product category
        //if no, then save it as a new category
        //then set it as a new product category.
        Category category = Optional.ofNullable(categoryRepository
                .findByName(productDTO.getCategory().getName())).orElseGet(
                () -> {
                    Category newCategory = new Category(productDTO.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        /*
        .orElseGet: if category not there then it will do the action but these action must return
        the same type of object that call it
        so if we say String.orElseGet(()->{ this must return String type});
        and above it will return category after saving it in db and will but it in category(object you made) and then
        product will take it as a setCategory
        all of this if category you're asking is not exist
         */
        productDTO.setCategory(category);
        return productRepository.save(productMapper.createProductFromProductDTO(productDTO, category));
    }


    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no product with id:" + productId
                )
        );
    }

    @Override
    @Transactional
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new GlobalNotFoundException(
                                    "there is no product with id:" + productId
                            );
                        });
//        Product product = new Product();
//        product = productRepository.findById(productId).orElseThrow(
//                () -> new GlobalNotFoundException(
//                        "there is no product with id:" + productId
//                )
//        );
//        productRepository.delete(product);
    }

    @Override
    @Transactional
    public Product updateProduct(ProductDTO productDTO, Long productId) {
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new GlobalNotFoundException("there is no product with id:" + productId)
//        );
//    return updateExistingProduct(product,productDTO).map(productRepository::save);
        return productRepository.findById(productId).map(
                //existingProduct is the one that will found from findById
                existingProduct -> updateExistingProduct(existingProduct, productDTO)
        ).map(productRepository::save).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no product with id:" + productId
                )
        );
        /*
        .map(productRepository::save)
        is like calling save method on the instance that call map function
        it mean
        .map(updatedProduct -> productRepository.save(updatedProduct))
         */
    }

    private Product updateExistingProduct(
            Product existingProduct,
            ProductDTO productDTO
    ) {
        existingProduct.setName(productDTO.getName());
        existingProduct.setBrand(productDTO.getBrand());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setInventory(productDTO.getInventory());
        existingProduct.setDescription(productDTO.getDescription());

        Category category = categoryRepository.findByName(productDTO.getCategory().getName());
        existingProduct.setCategory(category);

//        existingProduct = productMapper.createProductFromProductDTO(productDTO, category);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getAllProductsByBrandName(String brandName) {
        return productRepository.findByBrand(brandName);
    }

    @Override
    public List<Product> getAllProductsByCategoryNameAndBrandName(String categoryName, String brandName) {
        return productRepository.findByCategoryNameAndBrand(categoryName, brandName);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandNameAndProductName(String productName, String brandName) {
        return productRepository.findByBrandAndName(productName, brandName);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndProductName(String productName, String categoryName) {
        return productRepository.findByCategoryNameAndName(categoryName, productName);
    }

    @Override
    public Long countProductByBrandNameAndProductName(String productName, String brandName) {
        return productRepository.countByBrandAndName(productName, brandName);
    }

    @Override
    public Long countProductByCategoryNameAndProductName(String productName, String categoryName) {
        return productRepository.countByCategoryNameAndName(categoryName, productName);
    }
}
