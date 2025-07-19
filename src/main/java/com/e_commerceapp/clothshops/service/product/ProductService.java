package com.e_commerceapp.clothshops.service.product;

import com.e_commerceapp.clothshops.data.dto.ProductDTO;
import com.e_commerceapp.clothshops.exceptionhandler.AlreadyExistException;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.ProductMapper;
import com.e_commerceapp.clothshops.data.model.Category;
import com.e_commerceapp.clothshops.data.model.Product;
import com.e_commerceapp.clothshops.data.repository.CategoryRepository;
import com.e_commerceapp.clothshops.data.repository.ProductRepository;
import com.e_commerceapp.clothshops.rest.requests.ProductRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService  {

//TODO (WE NEED TO MAKE NOTE THAT PRODUCT IS OUT OF STACK FOR CHECKING)

    private final ProductRepository productRepository;


    private final CategoryRepository categoryRepository;


    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public Product addProduct(ProductRequests productRequests) {
        if (checkIfProductAlreadyExist(productRequests.getBrand(), productRequests.getName())) {
            throw new AlreadyExistException("there is a product with name: " + productRequests.getName() +
                    " and brand: " + productRequests.getBrand() + ", you may update this product instead!");
        }
        Category category = Optional.ofNullable(categoryRepository
                .findByName(productRequests.getCategory().getName())).orElseGet(
                () -> {
                    Category newCategory = new Category(productRequests.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        productRequests.setCategory(category);
        return productRepository.save(productMapper.createProductFromProductReq(productRequests));
    }

    private boolean checkIfProductAlreadyExist(String productBrand, String productName) {
        return productRepository.existsByBrandAndName(productBrand, productName);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no product with id:" + productId
                )
        );
    }

    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new GlobalNotFoundException(
                                    "there is no product with id:" + productId
                            );
                        });
    }

    public ProductDTO updateProduct(ProductRequests productRequests, Long productId) {
        Product product = productRepository.findById(productId).map(
                existingProduct -> updateExistingProduct(existingProduct, productRequests)
        ).map(productRepository::save).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no product with id: " + productId
                )
        );
        return productMapper.createProductDTOFromProduct(product);
    }

    private Product updateExistingProduct(
            Product existingProduct,
            ProductRequests productRequests
    ) {
        existingProduct.setName(productRequests.getName());
        existingProduct.setBrand(productRequests.getBrand());
        existingProduct.setPrice(productRequests.getPrice());
        existingProduct.setInventory(productRequests.getInventory());
        existingProduct.setDescription(productRequests.getDescription());

        Category category = categoryRepository.findByName(productRequests.getCategory().getName());
        existingProduct.setCategory(category);


        return existingProduct;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public List<ProductDTO> getAllProductsByCategoryName(String categoryName) {
        List<Product> products = productRepository.findByCategoryName(categoryName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public List<ProductDTO> getAllProductsByBrandName(String brandName) {
        List<Product> products = productRepository.findByBrand(brandName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public List<ProductDTO> getAllProductsByCategoryNameAndBrandName(String categoryName, String brandName) {
        List<Product> products = productRepository.findByCategoryNameAndBrand(categoryName, brandName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public List<ProductDTO> getProductsByName(String productName) {
        List<Product> products = productRepository.findByName(productName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public List<ProductDTO> getProductsByBrandNameAndProductName(String productName, String brandName) {
        List<Product> products = productRepository.findByBrandAndName(productName, brandName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public List<ProductDTO> getProductsByCategoryNameAndProductName(String productName, String categoryName) {
        List<Product> products = productRepository.findByCategoryNameAndName(categoryName, productName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    public Long countProductByBrandNameAndProductName(String productName, String brandName) {
        return productRepository.countByBrandAndName(productName, brandName);
    }

    public Long countProductByCategoryNameAndProductName(String productName, String categoryName) {
        return productRepository.countByCategoryNameAndName(categoryName, productName);
    }
}
